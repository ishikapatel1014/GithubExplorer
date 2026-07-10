package com.ishika.githubexplorer.domain.model

data class GitHubRepository(
    val id: Long,
    val name: String,
    val description: String?,
    val updatedAt: String,
    val stars: Int,
    val forks: Int,
    val repositoryUrl: String,
    val language: String?,
    val isFork: Boolean
)