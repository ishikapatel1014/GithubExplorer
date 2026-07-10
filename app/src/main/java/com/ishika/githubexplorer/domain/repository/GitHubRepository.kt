package com.ishika.githubexplorer.domain.repository

import com.ishika.githubexplorer.core.network.AppResult
import com.ishika.githubexplorer.domain.model.GitHubSearchResult
import kotlinx.coroutines.flow.Flow

interface GitHubRepository {

    val recentSearches: Flow<List<String>>

    suspend fun searchUser(
        username: String
    ): AppResult<GitHubSearchResult>

    suspend fun saveSearch(
        username: String
    )

    suspend fun clearSearchHistory()
}