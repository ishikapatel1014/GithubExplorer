package com.ishika.githubexplorer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ishika.githubexplorer.presentation.home.UiError
import com.ishika.githubexplorer.presentation.theme.GitHubExplorerTheme

@Composable
fun ErrorState(
    error: UiError,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val presentation = error.toPresentation()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 42.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            modifier = Modifier.size(104.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.errorContainer
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = presentation.icon,
                    contentDescription = null,
                    modifier = Modifier.size(52.dp),
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Text(
            text = presentation.title,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Text(
            text = presentation.message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Try again")
        }
    }
}

private data class ErrorPresentation(
    val icon: ImageVector,
    val title: String,
    val message: String
)

private fun UiError.toPresentation(): ErrorPresentation {
    return when (this) {
        UiError.USER_NOT_FOUND -> ErrorPresentation(
            icon = Icons.Outlined.PersonOff,
            title = "User not found",
            message = "We couldn't find a GitHub account with that username."
        )

        UiError.NO_INTERNET -> ErrorPresentation(
            icon = Icons.Outlined.CloudOff,
            title = "No internet connection",
            message = "Check your connection and try the search again."
        )

        UiError.RATE_LIMITED -> ErrorPresentation(
            icon = Icons.Outlined.WarningAmber,
            title = "Rate limit reached",
            message = "GitHub is temporarily limiting requests. Please wait and try again."
        )

        UiError.TIMEOUT -> ErrorPresentation(
            icon = Icons.Outlined.Schedule,
            title = "Request timed out",
            message = "GitHub took too long to respond. Please try again."
        )

        UiError.SERVER_ERROR -> ErrorPresentation(
            icon = Icons.Outlined.ErrorOutline,
            title = "GitHub is unavailable",
            message = "The GitHub service is temporarily unavailable."
        )

        UiError.UNKNOWN -> ErrorPresentation(
            icon = Icons.Outlined.ErrorOutline,
            title = "Something went wrong",
            message = "An unexpected error occurred. Please try again."
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorStatePreview() {
    GitHubExplorerTheme {
        ErrorState(
            error = UiError.NO_INTERNET,
            onRetry = {}
        )
    }
}