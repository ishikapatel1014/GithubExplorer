package com.ishika.githubexplorer.core.network

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

fun Throwable.toAppError(): AppError {
    return when (this) {
        is SocketTimeoutException -> AppError.Timeout
        is IOException -> AppError.NoInternet

        is HttpException -> {
            when (code()) {
                404 -> AppError.UserNotFound
                403 -> AppError.RateLimited
                in 500..599 -> AppError.ServerError
                else -> AppError.Unknown(this)
            }
        }

        else -> AppError.Unknown(this)
    }
}