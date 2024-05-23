package com.example.musicplayer.download


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import com.example.musicplayer.data.model.Track
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@SuppressLint("UnsafeOptInUsageError")
class DownloadManagerImpl(
    context: Context
) {
    private val downloadManager: DownloadManager
    private val downloadCache: SimpleCache
    private val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

    init {
        val databaseProvider = StandaloneDatabaseProvider(context)
        downloadCache = getSimpleCache(context, databaseProvider)

        val downloadExecutor = Executors.newFixedThreadPool(3)

        downloadManager = DownloadManager(
            context,
            databaseProvider,
            downloadCache,
            dataSourceFactory,
            downloadExecutor
        )

        downloadManager.maxParallelDownloads = 3
    }
    companion object {
        @Volatile
        private var simpleCache: SimpleCache? = null

        fun getSimpleCache(context: Context, databaseProvider: StandaloneDatabaseProvider): SimpleCache {
            return simpleCache ?: synchronized(this) {
                simpleCache ?: SimpleCache(
                    File(context.cacheDir, "downloads"),
                    NoOpCacheEvictor(),
                    databaseProvider
                ).also { simpleCache = it }
            }
        }
    }

    fun getDownloadedManager(): DownloadManager {
        return downloadManager
    }

    fun getDownloadCache(): SimpleCache {
        return downloadCache
    }
}