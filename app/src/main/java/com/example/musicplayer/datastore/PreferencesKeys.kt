package com.example.musicplayer.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferencesKeys {
    val IS_COMPLETED_KEY = booleanPreferencesKey("is_completed")
    val IS_SHOW = booleanPreferencesKey("is_show")
}