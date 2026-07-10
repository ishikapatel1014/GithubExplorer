package com.ishika.githubexplorer.data.remote

import com.ishika.githubexplorer.data.remote.dto.GitHubRepositoryDto
import com.ishika.githubexplorer.data.remote.dto.GitHubUserDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): GitHubUserDto

    @GET("users/{username}/repos")
    suspend fun getRepositories(
        @Path("username") username: String,
        @Query("type") type: String = "owner",
        @Query("sort") sort: String = "updated",
        @Query("per_page") perPage: Int = 100
    ): List<GitHubRepositoryDto>
}