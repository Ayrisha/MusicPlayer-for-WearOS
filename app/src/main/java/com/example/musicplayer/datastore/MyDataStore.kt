package com.example.musicplayer.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.myDataStore: DataStore<Preferences> by preferencesDataStore("settings")

class MyDataStore(
    context: Context
) {
    private val myDataStore: DataStore<Preferences> = context.myDataStore

    val recentSearches: Flow<List<String>> = myDataStore.data
        .map { preferences ->
            val searches = preferences[PreferencesKeys.RECENT_SEARCHES_KEY]
            searches?.split(";")?.map { it.trim() } ?: emptyList()
        }

    val refreshToken: Flow<String?> = myDataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.REFRESH_TOKEN_KEY]
        }

    val accessToken: Flow<String?> = myDataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN_KEY]
        }


    suspend fun updateRecentSearches(searches: List<String>) {
        try {
            Log.d("MyDataStore", "Updating recent searches: $searches")
            myDataStore.edit { preferences ->
                Log.d("MyDataStore", "Edit start: ${preferences[PreferencesKeys.RECENT_SEARCHES_KEY]}")
                preferences[PreferencesKeys.RECENT_SEARCHES_KEY] = searches.joinToString(separator = ";")
                Log.d("MyDataStore", "Edit complete: ${preferences[PreferencesKeys.RECENT_SEARCHES_KEY]}")
            }
            val preferences = myDataStore.data.first { true }
            val savedSearches = preferences[PreferencesKeys.RECENT_SEARCHES_KEY]
            Log.d("MyDataStore", "Saved searches after update: $savedSearches")
        } catch (e: Exception) {
            Log.e("MyDataStore", "Error updating recent searches: $e")
        }
    }

    suspend fun updateRefreshToken(refreshToken: String) {
        myDataStore.edit { preferences ->
            preferences[PreferencesKeys.REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    suspend fun updateAccessToken(accessToken: String) {
        myDataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN_KEY] = accessToken
        }
    }

    suspend fun clearTokens() {
        myDataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.REFRESH_TOKEN_KEY)
            preferences.remove(PreferencesKeys.ACCESS_TOKEN_KEY)
        }
    }
}
