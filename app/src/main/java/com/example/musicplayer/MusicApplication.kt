package com.example.musicplayer

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.DownloadManager
import com.example.musicplayer.datastore.DataStoreManager
import java.io.File
import java.util.concurrent.Executor

class MusicApplication: Application() {
    lateinit var container: AppContainer

    @OptIn(UnstableApi::class) override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}