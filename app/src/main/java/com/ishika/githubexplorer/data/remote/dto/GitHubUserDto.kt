package com.ishika.githubexplorer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GitHubUserDto(
    @SerializedName("login")
    val login: String,

    @SerializedName("name")
    val name: String?,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("html_url")
    val profileUrl: String,

    @SerializedName("public_repos")
    val publicRepos: Int
)