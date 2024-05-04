package com.example.musicplayer

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import com.example.musicplayer.data.AppContainer
import com.example.musicplayer.data.DefaultAppContainer
import com.example.musicplayer.data.datastore.DataStoreManager

class MusicApplication: Application() {
    lateinit var container: AppContainer
    @SuppressLint("UnsafeOptInUsageError")
    @OptIn(UnstableApi::class) override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        DataStoreManager.initialize(this)
    }
}