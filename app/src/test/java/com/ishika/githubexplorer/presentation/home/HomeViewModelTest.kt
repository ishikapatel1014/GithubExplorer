package com.ishika.githubexplorer.presentation.home

import com.ishika.githubexplorer.MainDispatcherRule
import com.ishika.githubexplorer.core.network.AppError
import com.ishika.githubexplorer.core.network.AppResult
import com.ishika.githubexplorer.domain.model.GitHubSearchResult
import com.ishika.githubexplorer.domain.model.GitHubUser
import com.ishika.githubexplorer.domain.repository.GitHubRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.ishika.githubexplorer.domain.model.GitHubRepository as GitHubRepositoryModel

private val testUser = GitHubUser(
    login = "octocat",
    name = "The Octocat",
    avatarUrl = "https://example.com/avatar.png",
    profileUrl = "https://github.com/octocat",
    publicRepos = 1
)

private val testRepository = GitHubRepositoryModel(
    id = 1L,
    name = "Hello-World",
    description = "My first repository on GitHub.",
    updatedAt = "2017-08-14T08:08:10Z",
    stars = 1421,
    forks = 1176,
    repositoryUrl = "https://github.com/octocat/Hello-World",
    language = "Kotlin",
    isFork = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeGitHubRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        repository = FakeGitHubRepository()
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `blank query displays validation error`() = runTest {
        viewModel.onQueryChanged("   ")

        viewModel.search()

        assertEquals(
            "Enter a GitHub username",
            viewModel.uiState.value.queryError
        )

        assertEquals(
            HomeContentState.Idle,
            viewModel.uiState.value.contentState
        )

        assertEquals(
            0,
            repository.searchCallCount
        )
    }

    @Test
    fun `query change clears validation error`() = runTest {
        viewModel.search()

        assertEquals(
            "Enter a GitHub username",
            viewModel.uiState.value.queryError
        )

        viewModel.onQueryChanged("octocat")

        assertNull(viewModel.uiState.value.queryError)
        assertEquals(
            "octocat",
            viewModel.uiState.value.query
        )
    }

    @Test
    fun `successful search displays user and repositories`() = runTest {
        repository.searchResult = AppResult.Success(
            GitHubSearchResult(
                user = testUser,
                repositories = listOf(testRepository)
            )
        )

        viewModel.onQueryChanged(" octocat ")
        viewModel.search()

        advanceUntilIdle()

        val state = viewModel.uiState.value
        val contentState =
            state.contentState as HomeContentState.Content

        assertEquals(
            "octocat",
            repository.lastSearchedUsername
        )

        assertEquals(
            testUser,
            contentState.user
        )

        assertEquals(
            listOf(testRepository),
            contentState.repositories
        )

        assertEquals(
            "octocat",
            state.lastSearchedUsername
        )

        assertFalse(state.isRefreshing)
        assertNull(state.refreshError)
    }

    @Test
    fun `successful search with no repositories displays empty state`() =
        runTest {
            repository.searchResult = AppResult.Success(
                GitHubSearchResult(
                    user = testUser,
                    repositories = emptyList()
                )
            )

            viewModel.onQueryChanged("octocat")
            viewModel.search()

            advanceUntilIdle()

            val emptyState =
                viewModel.uiState.value.contentState
                        as HomeContentState.Empty

            assertEquals(
                testUser,
                emptyState.user
            )
        }

    @Test
    fun `user not found maps to user not found UI error`() = runTest {
        repository.searchResult =
            AppResult.Failure(AppError.UserNotFound)

        viewModel.onQueryChanged("missing-user")
        viewModel.search()

        advanceUntilIdle()

        val errorState =
            viewModel.uiState.value.contentState
                    as HomeContentState.Error

        assertEquals(
            UiError.USER_NOT_FOUND,
            errorState.error
        )
    }

    @Test
    fun `no internet maps to no internet UI error`() = runTest {
        repository.searchResult =
            AppResult.Failure(AppError.NoInternet)

        viewModel.onQueryChanged("octocat")
        viewModel.search()

        advanceUntilIdle()

        val errorState =
            viewModel.uiState.value.contentState
                    as HomeContentState.Error

        assertEquals(
            UiError.NO_INTERNET,
            errorState.error
        )
    }

    @Test
    fun `rate limit maps to rate limited UI error`() = runTest {
        repository.searchResult =
            AppResult.Failure(AppError.RateLimited)

        viewModel.onQueryChanged("octocat")
        viewModel.search()

        advanceUntilIdle()

        val errorState =
            viewModel.uiState.value.contentState
                    as HomeContentState.Error

        assertEquals(
            UiError.RATE_LIMITED,
            errorState.error
        )
    }

    @Test
    fun `refresh failure preserves content and exposes refresh error`() =
        runTest {
            repository.searchResult = AppResult.Success(
                GitHubSearchResult(
                    user = testUser,
                    repositories = listOf(testRepository)
                )
            )

            viewModel.onQueryChanged("octocat")
            viewModel.search()
            advanceUntilIdle()

            repository.searchResult =
                AppResult.Failure(AppError.Timeout)

            viewModel.refresh()
            advanceUntilIdle()

            val state = viewModel.uiState.value

            assertTrue(
                state.contentState is HomeContentState.Content
            )

            assertFalse(state.isRefreshing)

            assertEquals(
                UiError.TIMEOUT,
                state.refreshError
            )
        }

    @Test
    fun `dismiss refresh error clears refresh error`() = runTest {
        repository.searchResult = AppResult.Success(
            GitHubSearchResult(
                user = testUser,
                repositories = listOf(testRepository)
            )
        )

        viewModel.onQueryChanged("octocat")
        viewModel.search()
        advanceUntilIdle()

        repository.searchResult =
            AppResult.Failure(AppError.ServerError)

        viewModel.refresh()
        advanceUntilIdle()

        assertEquals(
            UiError.SERVER_ERROR,
            viewModel.uiState.value.refreshError
        )

        viewModel.dismissRefreshError()

        assertNull(
            viewModel.uiState.value.refreshError
        )
    }

    @Test
    fun `search from history updates query and performs search`() = runTest {
        repository.searchResult = AppResult.Success(
            GitHubSearchResult(
                user = testUser,
                repositories = listOf(testRepository)
            )
        )

        viewModel.searchFromHistory("octocat")

        advanceUntilIdle()

        assertEquals(
            "octocat",
            viewModel.uiState.value.query
        )

        assertEquals(
            "octocat",
            repository.lastSearchedUsername
        )

        assertEquals(
            1,
            repository.searchCallCount
        )
    }

    @Test
    fun `recent searches are exposed in UI state`() = runTest {
        repository.emitRecentSearches(
            listOf(
                "octocat",
                "google"
            )
        )

        advanceUntilIdle()

        assertEquals(
            listOf(
                "octocat",
                "google"
            ),
            viewModel.uiState.value.recentSearches
        )
    }

    @Test
    fun `clear search history delegates to repository`() = runTest {
        viewModel.clearSearchHistory()

        advanceUntilIdle()

        assertEquals(
            1,
            repository.clearHistoryCallCount
        )
    }

    private class FakeGitHubRepository : GitHubRepository {

        private val recentSearchesState =
            MutableStateFlow<List<String>>(emptyList())

        override val recentSearches: Flow<List<String>> =
            recentSearchesState

        var searchResult: AppResult<GitHubSearchResult> =
            AppResult.Success(
                GitHubSearchResult(
                    user = testUser,
                    repositories = listOf(testRepository)
                )
            )

        var searchCallCount: Int = 0
            private set

        var clearHistoryCallCount: Int = 0
            private set

        var lastSearchedUsername: String? = null
            private set

        override suspend fun searchUser(
            username: String
        ): AppResult<GitHubSearchResult> {
            searchCallCount++
            lastSearchedUsername = username
            return searchResult
        }

        override suspend fun saveSearch(username: String) {
            // Saving is handled by the repository implementation.
            // It is not needed for these ViewModel tests.
        }

        override suspend fun clearSearchHistory() {
            clearHistoryCallCount++
            recentSearchesState.value = emptyList()
        }

        fun emitRecentSearches(searches: List<String>) {
            recentSearchesState.value = searches
        }
    }
}