package com.ishika.githubexplorer.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ishika.githubexplorer.domain.model.GitHubRepository
import com.ishika.githubexplorer.presentation.theme.GitHubExplorerTheme
import java.util.Locale

@Composable
fun RepositoryCard(
    repository: GitHubRepository,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(46.dp),
                shape = RoundedCornerShape(13.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Outlined.Folder,
                    contentDescription = null,
                    modifier = Modifier.padding(11.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.size(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = repository.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = repository.description
                        ?.takeIf { it.isNotBlank() }
                        ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RepositoryMetric(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        value = repository.stars.toCompactNumber()
                    )

                    RepositoryMetric(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.CallSplit,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        value = repository.forks.toCompactNumber()
                    )

                    repository.language
                        ?.takeIf { it.isNotBlank() }
                        ?.let { language ->
                            LanguageBadge(language = language)
                        }
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Open ${repository.name}",
                modifier = Modifier.size(22.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RepositoryMetric(
    icon: @Composable () -> Unit,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        icon()

        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LanguageBadge(
    language: String
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Text(
            text = language,
            modifier = Modifier.padding(
                horizontal = 9.dp,
                vertical = 4.dp
            ),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun Int.toCompactNumber(): String {
    return when {
        this >= 1_000_000 -> {
            String.format(
                Locale.getDefault(),
                "%.1fM",
                this / 1_000_000f
            )
        }

        this >= 1_000 -> {
            String.format(
                Locale.getDefault(),
                "%.1fK",
                this / 1_000f
            )
        }

        else -> toString()
    }
}

@Preview(showBackground = true)
@Composable
private fun RepositoryCardPreview() {
    GitHubExplorerTheme {
        RepositoryCard(
            repository = GitHubRepository(
                id = 1L,
                name = "GitHubExplorer",
                description = "A modern GitHub profile and repository explorer built with Kotlin and Jetpack Compose.",
                updatedAt = "2026-07-10T14:30:00Z",
                stars = 1421,
                forks = 1176,
                repositoryUrl = "https://github.com/octocat/GitHubExplorer",
                language = "Kotlin",
                isFork = false
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}