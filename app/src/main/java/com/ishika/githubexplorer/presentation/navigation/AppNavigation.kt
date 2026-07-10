package com.ishika.githubexplorer.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.ishika.githubexplorer.domain.model.GitHubRepository
import com.ishika.githubexplorer.presentation.details.RepositoryDetailScreen
import com.ishika.githubexplorer.presentation.home.HomeRoute

private object Routes {
    const val HOME = "home"

    const val REPOSITORY_ARGUMENT = "repository"

    const val REPOSITORY_DETAILS =
        "repository_details/{$REPOSITORY_ARGUMENT}"

    fun repositoryDetails(repositoryJson: String): String {
        return "repository_details/${Uri.encode(repositoryJson)}"
    }
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val gson = Gson()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable(route = Routes.HOME) {
            HomeRoute(
                onRepositoryClick = { repository ->
                    val repositoryJson = gson.toJson(repository)

                    navController.navigate(
                        Routes.repositoryDetails(repositoryJson)
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Routes.REPOSITORY_DETAILS,
            arguments = listOf(
                navArgument(Routes.REPOSITORY_ARGUMENT) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val repositoryJson =
                backStackEntry.arguments
                    ?.getString(Routes.REPOSITORY_ARGUMENT)

            val repository = repositoryJson
                ?.let(Uri::decode)
                ?.let { decodedJson ->
                    runCatching {
                        gson.fromJson(
                            decodedJson,
                            GitHubRepository::class.java
                        )
                    }.getOrNull()
                }

            if (repository != null) {
                RepositoryDetailScreen(
                    repository = repository,
                    onBackClick = {
                        navController.navigateUp()
                    }
                )
            } else {
                navController.navigateUp()
            }
        }
    }
}