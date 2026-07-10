package com.ishika.githubexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ishika.githubexplorer.presentation.navigation.AppNavigation
import com.ishika.githubexplorer.presentation.theme.GitHubExplorerTheme
import com.ishika.githubexplorer.presentation.theme.configureSystemBars

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureSystemBars()

        setContent {
            GitHubExplorerTheme {
                AppNavigation()
            }
        }
    }
}