package com.example.musicplayer.data.download

import android.annotation.SuppressLint
import android.content.Context
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.DownloadManager
import java.io.File
import java.util.concurrent.Executor

class DownloadManagerImpI(
    private val context: Context,
    private val title: String
) {
    @SuppressLint("UnsafeOptInUsageError")
    val databaseProvider = StandaloneDatabaseProvider(context)
    @SuppressLint("UnsafeOptInUsageError")
    //val downloadCache = SimpleCache(File(getDownloadDirectory(context), DOWNLOAD_CONTENT_DIRECTORY), NoOpCacheEvictor(), databaseProvider)
    private val dataSourceFactory = DefaultHttpDataSource.Factory()
    private val downloadExecutor = Executor(Runnable::run)
//    private val downloadManager =
//        DownloadManager(
//            context,
//            databaseProvider,
//            downloadCache,
//            dataSourceFactory,
//            downloadExecutor
//        )
}