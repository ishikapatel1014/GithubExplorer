package com.ishika.githubexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GitHubRepositoryDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("stargazers_count")
    val stars: Int,

    @SerializedName("forks_count")
    val forks: Int,

    @SerializedName("html_url")
    val repositoryUrl: String,

    @SerializedName("language")
    val language: String?,

    @SerializedName("fork")
    val isFork: Boolean
)