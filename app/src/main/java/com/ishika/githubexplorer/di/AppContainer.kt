package com.ishika.githubexplorer.di

import android.content.Context
import com.ishika.githubexplorer.data.local.SearchHistoryDataSource
import com.ishika.githubexplorer.data.remote.GitHubApiService
import com.ishika.githubexplorer.data.repository.GitHubRepositoryImpl
import com.ishika.githubexplorer.domain.repository.GitHubRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer(context: Context) {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: GitHubApiService =
        retrofit.create(GitHubApiService::class.java)

    private val searchHistoryDataSource =
        SearchHistoryDataSource(context)

    val gitHubRepository: GitHubRepository =
        GitHubRepositoryImpl(
            apiService = apiService,
            searchHistoryDataSource = searchHistoryDataSource
        )
}