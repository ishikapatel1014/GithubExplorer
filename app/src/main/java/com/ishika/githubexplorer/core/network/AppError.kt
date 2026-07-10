package com.ishika.githubexplorer.core.network

sealed interface AppError {

    data object UserNotFound : AppError

    data object RateLimited : AppError

    data object NoInternet : AppError

    data object Timeout : AppError

    data object ServerError : AppError

    data class Unknown(
        val cause: Throwable? = null
    ) : AppError
}