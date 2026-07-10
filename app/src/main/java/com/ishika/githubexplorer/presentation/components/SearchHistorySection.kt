package com.ishika.githubexplorer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ishika.githubexplorer.presentation.theme.GitHubExplorerTheme

@Composable
fun SearchHistorySection(
    recentSearches: List<String>,
    onSearchSelected: (String) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (recentSearches.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Recent searches",
                style = MaterialTheme.typography.titleMedium
            )

            TextButton(
                onClick = onClearHistory
            ) {
                Text(text = "Clear")
            }
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            recentSearches.forEach { username ->
                AssistChip(
                    onClick = {
                        onSearchSelected(username)
                    },
                    label = {
                        Text(text = username)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchHistorySectionPreview() {
    GitHubExplorerTheme {
        SearchHistorySection(
            recentSearches = listOf(
                "octocat",
                "google",
                "square"
            ),
            onSearchSelected = {},
            onClearHistory = {}
        )
    }
}