package com.ishika.githubexplorer.presentation.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ishika.githubexplorer.core.util.toReadableRepositoryDate
import com.ishika.githubexplorer.domain.model.GitHubRepository
import com.ishika.githubexplorer.presentation.theme.GitHubExplorerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailScreen(
    repository: GitHubRepository,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Repository details")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(repository.repositoryUrl)
                    )
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = null
                )

                Text(
                    text = "Open on GitHub",
                    modifier = Modifier.padding(
                        start = 8.dp
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = repository.name,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = repository.description
                    ?: "No description available.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            RepositoryStatisticsCard(
                repository = repository
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = repository.description
                    ?: "This repository does not provide a description.",
                style = MaterialTheme.typography.bodyLarge
            )

            if (repository.isFork) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "This repository is a fork.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun RepositoryStatisticsCard(
    repository: GitHubRepository,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RepositoryDetailRow(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null
                    )
                },
                label = "Last updated",
                value = repository.updatedAt.toReadableRepositoryDate()
            )

            HorizontalDivider()

            RepositoryDetailRow(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null
                    )
                },
                label = "Stars",
                value = repository.stars.toString()
            )

            HorizontalDivider()

            RepositoryDetailRow(
                icon = {
                    Icon(
                        imageVector = Icons.Default.CallSplit,
                        contentDescription = null
                    )
                },
                label = "Forks",
                value = repository.forks.toString()
            )

            HorizontalDivider()

            RepositoryDetailRow(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null
                    )
                },
                label = "Language",
                value = repository.language ?: "Not specified"
            )
        }
    }
}

@Composable
private fun RepositoryDetailRow(
    icon: @Composable () -> Unit,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RepositoryDetailScreenPreview() {
    GitHubExplorerTheme {
        RepositoryDetailScreen(
            repository = GitHubRepository(
                id = 1L,
                name = "EventsExplorer",
                description = "Android application built with Kotlin and Jetpack Compose.",
                updatedAt = "2026-07-10T14:30:00Z",
                stars = 152,
                forks = 24,
                repositoryUrl = "https://github.com/example/EventsExplorer",
                language = "Kotlin",
                isFork = false
            ),
            onBackClick = {}
        )
    }
}