package com.ishika.githubexplorer.domain.model

data class GitHubSearchResult(
    val user: GitHubUser,
    val repositories: List<GitHubRepository>
)