package com.ishika.githubexplorer.domain.model

data class GitHubUser(
    val login: String,
    val name: String,
    val avatarUrl: String,
    val profileUrl: String,
    val publicRepos: Int
)