package com.ishika.githubexplorer.core.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun String.toReadableRepositoryDate(): String {
    return try {
        val inputFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            Locale.getDefault()
        )
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val outputFormat = SimpleDateFormat(
            "MMM dd, yyyy",
            Locale.getDefault()
        )

        val date = inputFormat.parse(this)

        if (date != null) {
            outputFormat.format(date)
        } else {
            this
        }
    } catch (e: Exception) {
        this
    }
}