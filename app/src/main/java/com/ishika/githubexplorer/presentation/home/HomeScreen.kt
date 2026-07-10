package com.ishika.githubexplorer.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ishika.githubexplorer.GitHubExplorerApplication
import com.ishika.githubexplorer.domain.model.GitHubRepository
import com.ishika.githubexplorer.presentation.components.GitHubSearchBar
import com.ishika.githubexplorer.presentation.components.SearchHistorySection

@Composable
fun HomeRoute(
    onRepositoryClick: (GitHubRepository) -> Unit,
    modifier: Modifier = Modifier
) {
    val application =
        LocalContext.current.applicationContext as GitHubExplorerApplication

    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(
            repository = application.appContainer.gitHubRepository
        )
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onQueryChange = viewModel::onQueryChanged,
        onSearch = viewModel::search,
        onHistorySelected = viewModel::searchFromHistory,
        onClearHistory = viewModel::clearSearchHistory,
        onRetry = viewModel::retry,
        onRefresh = viewModel::refresh,
        onDismissRefreshError = viewModel::dismissRefreshError,
        onRepositoryClick = onRepositoryClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onHistorySelected: (String) -> Unit,
    onClearHistory: () -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onDismissRefreshError: () -> Unit,
    onRepositoryClick: (GitHubRepository) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.refreshError) {
        val refreshError =
            uiState.refreshError ?: return@LaunchedEffect

        snackbarHostState.showSnackbar(
            message = refreshError.toDisplayMessage()
        )

        onDismissRefreshError()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Code,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "GitHub Explorer",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 20.dp,
                    top = 8.dp,
                    end = 20.dp,
                    bottom = 8.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GitHubSearchBar(
                    query = uiState.query,
                    error = uiState.queryError,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        focusManager.clearFocus()
                        onSearch()
                    }
                )

                SearchHistorySection(
                    recentSearches = uiState.recentSearches,
                    onSearchSelected = { username ->
                        focusManager.clearFocus()
                        onHistorySelected(username)
                    },
                    onClearHistory = onClearHistory
                )
            }

            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize()
            ) {
                HomeContent(
                    contentState = uiState.contentState,
                    onRetry = onRetry,
                    onRepositoryClick = onRepositoryClick,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

private fun UiError.toDisplayMessage(): String {
    return when (this) {
        UiError.USER_NOT_FOUND ->
            "GitHub user not found."

        UiError.RATE_LIMITED ->
            "GitHub API rate limit reached. Try again later."

        UiError.NO_INTERNET ->
            "No internet connection."

        UiError.TIMEOUT ->
            "The refresh request timed out."

        UiError.SERVER_ERROR ->
            "GitHub is temporarily unavailable."

        UiError.UNKNOWN ->
            "Unable to refresh repositories."
    }
}