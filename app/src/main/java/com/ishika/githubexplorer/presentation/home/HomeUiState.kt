package com.ishika.githubexplorer.presentation.home

import com.ishika.githubexplorer.domain.model.GitHubRepository
import com.ishika.githubexplorer.domain.model.GitHubUser

data class HomeUiState(
    val query: String = "",
    val contentState: HomeContentState = HomeContentState.Idle,
    val recentSearches: List<String> = emptyList(),
    val lastSearchedUsername: String? = null,
    val queryError: String? = null,
    val isRefreshing: Boolean = false,
    val refreshError: UiError? = null
)

sealed interface HomeContentState {

    data object Idle : HomeContentState

    data object Loading : HomeContentState

    data class Content(
        val user: GitHubUser,
        val repositories: List<GitHubRepository>
    ) : HomeContentState

    data class Empty(
        val user: GitHubUser
    ) : HomeContentState

    data class Error(
        val error: UiError
    ) : HomeContentState
}

enum class UiError {
    USER_NOT_FOUND,
    RATE_LIMITED,
    NO_INTERNET,
    TIMEOUT,
    SERVER_ERROR,
    UNKNOWN
}