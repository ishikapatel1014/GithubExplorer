package com.ishika.githubexplorer.data.repository

import com.ishika.githubexplorer.core.network.AppResult
import com.ishika.githubexplorer.core.network.toAppError
import com.ishika.githubexplorer.data.local.SearchHistoryDataSource
import com.ishika.githubexplorer.data.remote.GitHubApiService
import com.ishika.githubexplorer.data.remote.dto.toDomain
import com.ishika.githubexplorer.domain.model.GitHubSearchResult
import com.ishika.githubexplorer.domain.repository.GitHubRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class GitHubRepositoryImpl(
    private val apiService: GitHubApiService,
    private val searchHistoryDataSource: SearchHistoryDataSource
) : GitHubRepository {

    override val recentSearches: Flow<List<String>> =
        searchHistoryDataSource.recentSearches

    override suspend fun searchUser(
        username: String
    ): AppResult<GitHubSearchResult> {
        return try {
            val result = coroutineScope {
                val userDeferred = async {
                    apiService.getUser(username)
                }

                val repositoriesDeferred = async {
                    apiService.getRepositories(username)
                }

                GitHubSearchResult(
                    user = userDeferred.await().toDomain(),
                    repositories = repositoriesDeferred
                        .await()
                        .map { it.toDomain() }
                )
            }

            saveSearch(username)

            AppResult.Success(result)
        } catch (throwable: Throwable) {
            AppResult.Failure(
                throwable.toAppError()
            )
        }
    }

    override suspend fun saveSearch(username: String) {
        searchHistoryDataSource.saveSearch(username)
    }

    override suspend fun clearSearchHistory() {
        searchHistoryDataSource.clearHistory()
    }
}