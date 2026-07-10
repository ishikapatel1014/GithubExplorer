package com.ishika.githubexplorer.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ishika.githubexplorer.presentation.theme.GitHubExplorerTheme

@Composable
fun LoadingState(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(
        label = "loading-transition"
    )

    val alpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "loading-alpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        ProfileSkeleton(alpha = alpha)

        repeat(3) {
            RepositorySkeleton(alpha = alpha)
        }
    }
}

@Composable
private fun ProfileSkeleton(
    alpha: Float
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SkeletonBox(
                modifier = Modifier.size(84.dp),
                shape = CircleShape,
                alpha = alpha
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .height(22.dp),
                    alpha = alpha
                )

                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth(0.38f)
                        .height(16.dp),
                    alpha = alpha
                )

                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(34.dp),
                    shape = RoundedCornerShape(50),
                    alpha = alpha
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        SkeletonBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            shape = RoundedCornerShape(16.dp),
            alpha = alpha
        )
    }
}

@Composable
private fun RepositorySkeleton(
    alpha: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        SkeletonBox(
            modifier = Modifier.size(44.dp),
            shape = RoundedCornerShape(12.dp),
            alpha = alpha
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(18.dp),
                alpha = alpha
            )

            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp),
                alpha = alpha
            )

            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(14.dp),
                alpha = alpha
            )
        }
    }
}

@Composable
private fun SkeletonBox(
    modifier: Modifier,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(8.dp),
    alpha: Float
) {
    Box(
        modifier = modifier
            .alpha(alpha)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = shape
            )
    )
}

@Preview(showBackground = true)
@Composable
private fun LoadingStatePreview() {
    GitHubExplorerTheme {
        LoadingState()
    }
}