package com.ishika.githubexplorer

import android.app.Application
import com.ishika.githubexplorer.di.AppContainer

class GitHubExplorerApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}