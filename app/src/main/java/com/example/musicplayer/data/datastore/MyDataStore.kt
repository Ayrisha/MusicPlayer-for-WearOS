package com.example.musicplayer.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.myDataStore: DataStore<Preferences> by preferencesDataStore("settings")

data class AuthStatus(
    val isCompleted: Boolean,
    val isShow: Boolean
)

class MyDataStore(
    context: Context
) {
    private val myDataStore: DataStore<Preferences> = context.myDataStore

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