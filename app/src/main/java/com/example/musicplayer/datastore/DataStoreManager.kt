package com.example.musicplayer.datastore

import android.content.Context

object DataStoreManager {
    fun getInstance(context: Context): MyDataStore {
        return MyDataStore(context)
    }
}