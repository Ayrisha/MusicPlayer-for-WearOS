package com.example.musicplayer.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.musicplayer.datastore.PreferencesKeys.RECENT_SEARCHES_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.myDataStore: DataStore<Preferences> by preferencesDataStore("settings")

class MyDataStore(
    context: Context
) {
    private val myDataStore: DataStore<Preferences> = context.myDataStore

    val recentSearches: Flow<List<String>> = myDataStore.data
        .map { preferences ->
            preferences[RECENT_SEARCHES_KEY]?.split(",")?.map { it.trim() } ?: emptyList()
        }

    suspend fun updateRecentSearches(searches: List<String>) {
        myDataStore.edit { preferences ->
            preferences[RECENT_SEARCHES_KEY] = searches.joinToString(",")
        }
    }

    val isCompleted: Flow<Boolean> = myDataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_COMPLETED_KEY] ?: false
        }

    val isShow: Flow<Boolean> = myDataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_SHOW] ?: false
        }

    suspend fun updateIsCompleted(isCompleted: Boolean){
        myDataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_COMPLETED_KEY] = isCompleted
        }
    }

    suspend fun updateIsShow(isShow: Boolean){
        myDataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_SHOW] = isShow
        }
    }
}