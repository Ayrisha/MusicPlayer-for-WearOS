package com.example.musicplayer.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val IS_COMPLETED_KEY = booleanPreferencesKey("is_completed")
    val IS_SHOW = booleanPreferencesKey("is_show")
    val RECENT_SEARCHES_KEY = stringPreferencesKey("recent_searches_list")
}