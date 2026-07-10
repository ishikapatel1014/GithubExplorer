package com.ishika.githubexplorer.data.remote.dto

import com.ishika.githubexplorer.domain.model.GitHubRepository
import com.ishika.githubexplorer.domain.model.GitHubUser

fun GitHubUserDto.toDomain(): GitHubUser {
    return GitHubUser(
        login = login,
        name = name?.takeIf { it.isNotBlank() } ?: login,
        avatarUrl = avatarUrl,
        profileUrl = profileUrl,
        publicRepos = publicRepos
    )
}

fun GitHubRepositoryDto.toDomain(): GitHubRepository {
    return GitHubRepository(
        id = id,
        name = name,
        description = description,
        updatedAt = updatedAt,
        stars = stars,
        forks = forks,
        repositoryUrl = repositoryUrl,
        language = language,
        isFork = isFork
    )
}