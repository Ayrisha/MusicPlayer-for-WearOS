package com.example.musicplayer.datastore

import android.content.Context

object DataStoreManager {
    private lateinit var instance: MyDataStore

    fun initialize(context: Context) {
        instance = MyDataStore(context)
    }

    fun getInstance(): MyDataStore {
        return instance
    }
}