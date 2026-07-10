package com.ishika.githubexplorer.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ishika.githubexplorer.core.network.AppError
import com.ishika.githubexplorer.core.network.AppResult
import com.ishika.githubexplorer.domain.model.GitHubSearchResult
import com.ishika.githubexplorer.domain.repository.GitHubRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: GitHubRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        observeSearchHistory()
    }

    fun onQueryChanged(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                query = query,
                queryError = null
            )
        }
    }

    fun search() {
        val username = _uiState.value.query.trim()

        if (username.isBlank()) {
            _uiState.update { currentState ->
                currentState.copy(
                    queryError = "Enter a GitHub username"
                )
            }
            return
        }

        performSearch(
            username = username,
            isRefresh = false
        )
    }

    fun searchFromHistory(username: String) {
        _uiState.update { currentState ->
            currentState.copy(
                query = username,
                queryError = null
            )
        }

        performSearch(
            username = username,
            isRefresh = false
        )
    }

    fun retry() {
        val username = _uiState.value.lastSearchedUsername
            ?: _uiState.value.query.trim()

        if (username.isBlank()) return

        performSearch(
            username = username,
            isRefresh = false
        )
    }

    fun refresh() {
        val username = _uiState.value.lastSearchedUsername
            ?: return

        if (_uiState.value.isRefreshing) return

        performSearch(
            username = username,
            isRefresh = true
        )
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            repository.clearSearchHistory()
        }
    }

    fun dismissRefreshError() {
        _uiState.update { currentState ->
            currentState.copy(
                refreshError = null
            )
        }
    }

    private fun performSearch(
        username: String,
        isRefresh: Boolean
    ) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            if (isRefresh) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isRefreshing = true,
                        refreshError = null
                    )
                }
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        contentState = HomeContentState.Loading,
                        lastSearchedUsername = username,
                        queryError = null,
                        refreshError = null
                    )
                }
            }

            when (
                val result = repository.searchUser(username)
            ) {
                is AppResult.Success -> {
                    handleSuccess(
                        result = result.data,
                        username = username
                    )
                }

                is AppResult.Failure -> {
                    handleFailure(
                        error = result.error,
                        isRefresh = isRefresh
                    )
                }
            }
        }
    }

    private fun handleSuccess(
        result: GitHubSearchResult,
        username: String
    ) {
        val contentState =
            if (result.repositories.isEmpty()) {
                HomeContentState.Empty(
                    user = result.user
                )
            } else {
                HomeContentState.Content(
                    user = result.user,
                    repositories = result.repositories
                )
            }

        _uiState.update { currentState ->
            currentState.copy(
                query = username,
                contentState = contentState,
                lastSearchedUsername = username,
                isRefreshing = false,
                refreshError = null
            )
        }
    }

    private fun handleFailure(
        error: AppError,
        isRefresh: Boolean
    ) {
        val uiError = error.toUiError()

        _uiState.update { currentState ->
            if (isRefresh) {
                currentState.copy(
                    isRefreshing = false,
                    refreshError = uiError
                )
            } else {
                currentState.copy(
                    contentState = HomeContentState.Error(uiError),
                    isRefreshing = false
                )
            }
        }
    }

    private fun observeSearchHistory() {
        viewModelScope.launch {
            repository.recentSearches
                .catch {
                    emit(emptyList())
                }
                .collect { searches ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            recentSearches = searches
                        )
                    }
                }
        }
    }
}

private fun AppError.toUiError(): UiError {
    return when (this) {
        AppError.UserNotFound -> UiError.USER_NOT_FOUND
        AppError.RateLimited -> UiError.RATE_LIMITED
        AppError.NoInternet -> UiError.NO_INTERNET
        AppError.Timeout -> UiError.TIMEOUT
        AppError.ServerError -> UiError.SERVER_ERROR
        is AppError.Unknown -> UiError.UNKNOWN
    }
}