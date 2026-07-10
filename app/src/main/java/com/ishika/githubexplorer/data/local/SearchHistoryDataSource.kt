package com.ishika.githubexplorer.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.searchHistoryDataStore by preferencesDataStore(
    name = "search_history"
)

class SearchHistoryDataSource(
    private val context: Context
) {

    private companion object {
        val RECENT_SEARCHES = stringPreferencesKey("recent_searches")
        const val MAX_HISTORY_SIZE = 5
        const val SEPARATOR = "|"
    }

    val recentSearches: Flow<List<String>> =
        context.searchHistoryDataStore.data.map { preferences ->
            preferences[RECENT_SEARCHES]
                .orEmpty()
                .split(SEPARATOR)
                .filter { it.isNotBlank() }
        }

    suspend fun saveSearch(username: String) {
        val normalizedUsername = username.trim()

        if (normalizedUsername.isBlank()) return

        context.searchHistoryDataStore.edit { preferences ->
            val currentSearches = preferences[RECENT_SEARCHES]
                .orEmpty()
                .split(SEPARATOR)
                .filter { it.isNotBlank() }
                .toMutableList()

            currentSearches.removeAll {
                it.equals(normalizedUsername, ignoreCase = true)
            }

            currentSearches.add(0, normalizedUsername)

            preferences[RECENT_SEARCHES] =
                currentSearches
                    .take(MAX_HISTORY_SIZE)
                    .joinToString(SEPARATOR)
        }
    }

    suspend fun clearHistory() {
        context.searchHistoryDataStore.edit { preferences ->
            preferences.remove(RECENT_SEARCHES)
        }
    }
}