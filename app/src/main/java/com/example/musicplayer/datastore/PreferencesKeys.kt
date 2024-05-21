package com.example.musicplayer.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val RECENT_SEARCHES_KEY = stringPreferencesKey("recent_searches_list")
    val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
}