package com.ishika.githubexplorer.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ishika.githubexplorer.domain.model.GitHubRepository
import com.ishika.githubexplorer.domain.model.GitHubUser
import com.ishika.githubexplorer.presentation.components.EmptyState
import com.ishika.githubexplorer.presentation.components.ErrorState
import com.ishika.githubexplorer.presentation.components.LoadingState
import com.ishika.githubexplorer.presentation.components.RepositoryCard
import com.ishika.githubexplorer.presentation.components.UserProfileCard
import com.ishika.githubexplorer.presentation.theme.GitHubExplorerTheme

@Composable
fun HomeContent(
    contentState: HomeContentState,
    onRetry: () -> Unit,
    onRepositoryClick: (GitHubRepository) -> Unit,
    modifier: Modifier = Modifier
) {
    when (contentState) {
        HomeContentState.Idle -> {
            IdleContent(modifier = modifier)
        }

        HomeContentState.Loading -> {
            LoadingState(modifier = modifier)
        }

        is HomeContentState.Error -> {
            ErrorState(
                error = contentState.error,
                onRetry = onRetry,
                modifier = modifier
            )
        }

        is HomeContentState.Empty -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item(key = "user-profile") {
                    UserProfileCard(user = contentState.user)
                }

                item(key = "empty-state") {
                    EmptyState()
                }
            }
        }

        is HomeContentState.Content -> {
            RepositoryContent(
                user = contentState.user,
                repositories = contentState.repositories,
                onRepositoryClick = onRepositoryClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun IdleContent(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 24.dp,
            vertical = 36.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(132.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }

        item {
            Text(
                text = "Explore GitHub",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }

        item {
            Text(
                text = "Search for any GitHub user to view their profile and public repositories.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Tip",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Try searching for “octocat”.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun RepositoryContent(
    user: GitHubUser,
    repositories: List<GitHubRepository>,
    onRepositoryClick: (GitHubRepository) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "user-profile") {
            UserProfileCard(user = user)
        }

        item(key = "repositories-heading") {
            Text(
                text = "Repositories",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(
                    top = 10.dp,
                    bottom = 2.dp
                )
            )
        }

        items(
            items = repositories,
            key = { repository -> repository.id }
        ) { repository ->
            RepositoryCard(
                repository = repository,
                onClick = {
                    onRepositoryClick(repository)
                }
            )
        }
    }
}

@Preview(
    name = "Welcome",
    showBackground = true
)
@Composable
private fun IdleContentPreview() {
    GitHubExplorerTheme {
        HomeContent(
            contentState = HomeContentState.Idle,
            onRetry = {},
            onRepositoryClick = {}
        )
    }
}

@Preview(
    name = "Repository content",
    showBackground = true
)
@Composable
private fun HomeContentPreview() {
    GitHubExplorerTheme {
        HomeContent(
            contentState = HomeContentState.Content(
                user = previewUser,
                repositories = previewRepositories
            ),
            onRetry = {},
            onRepositoryClick = {}
        )
    }
}

private val previewUser = GitHubUser(
    login = "octocat",
    name = "The Octocat",
    avatarUrl = "",
    profileUrl = "https://github.com/octocat",
    publicRepos = 8
)

private val previewRepositories = listOf(
    GitHubRepository(
        id = 1L,
        name = "Hello-World",
        description = "My first repository on GitHub.",
        updatedAt = "2017-08-14T08:08:10Z",
        stars = 1421,
        forks = 1176,
        repositoryUrl = "https://github.com/octocat/Hello-World",
        language = "Kotlin",
        isFork = false
    ),
    GitHubRepository(
        id = 2L,
        name = "Spoon-Knife",
        description = "This repository is for demonstration purposes.",
        updatedAt = "2025-05-02T10:15:30Z",
        stars = 13000,
        forks = 150000,
        repositoryUrl = "https://github.com/octocat/Spoon-Knife",
        language = "HTML",
        isFork = false
    )
)