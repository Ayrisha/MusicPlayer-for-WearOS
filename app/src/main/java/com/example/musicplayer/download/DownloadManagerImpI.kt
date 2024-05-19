package com.example.musicplayer.download


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@SuppressLint("UnsafeOptInUsageError")
class DownloadManagerImpl(
    context: Context
) {
    val downloadManager: DownloadManager
    val cache: SimpleCache

    init {
        val databaseProvider = StandaloneDatabaseProvider(context)
        val downloadCache = getSimpleCache(context, databaseProvider)
        cache = getSimpleCache(context, databaseProvider)
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

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
                    File(context.filesDir, "downloads"),
                    NoOpCacheEvictor(),
                    databaseProvider
                ).also { simpleCache = it }
            }
        }
    }

    @OptIn(ExperimentalHorologistApi::class)
    fun getDownloadedTracks(context: Context): List<DownloadedTrack> {
        val tracker = DownloadTracker(cache, downloadManager)
        return tracker.getDownloadedTracks()
    }
}