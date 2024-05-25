package com.example.musicplayer.download


import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.util.Log
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import java.io.File
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
        downloadManager.addListener(DownloadManagerListener())
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

    fun removeDownload(context: Context, downloadId: String) {
        DownloadService.sendRemoveDownload(
            context,
            MediaDownloadServiceImpl::class.java,
            downloadId,
            /* foreground= */ false
        )

        DownloadService.sendRemoveDownload(
            context,
            MediaDownloadServiceImpl::class.java,
            downloadId + "img",
            /* foreground= */ false
        )
    }

    fun addDownload(
        url: String,
        id: String,
        title: String,
        artist: String,
        imgUrl: String,
        context: Context
    ) {
        Log.d("DownloadTracker", "addDownload")

        val separator = ":"

        val dataString = "$title$separator$artist"

        val dataBytes = dataString.encodeToByteArray()

        val downloadTrackRequest =
            DownloadRequest.Builder(/* id = */ id, /* uri = */ Uri.parse(url))
                .setData(dataBytes)
                .build()

        val downloadImgRequest =
            DownloadRequest.Builder(/* id = */ id + "img", /* uri = */ Uri.parse(imgUrl))
                .build()

        downloadManager.addDownload(downloadTrackRequest)
        downloadManager.addDownload(downloadImgRequest)

        DownloadService.sendAddDownload(
            context,
            MediaDownloadServiceImpl::class.java,
            downloadTrackRequest,
            /* foreground= */ false
        )
    }

    fun isSongDownloaded(songId: String): Boolean {
        val downloads = downloadManager.downloadIndex.getDownloads(Download.STATE_COMPLETED)

        if (downloads.moveToFirst()) {
            do {
                val download = downloads.download
                val mediaId = download.request.id
                if (mediaId == songId) {
                    downloads.close()
                    return true
                }
            } while (downloads.moveToNext())
        }

        downloads.close()
        return false
    }

    private inner class DownloadManagerListener : DownloadManager.Listener {
        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: Exception?
        ) {
            when (download.state) {
                Download.STATE_QUEUED -> Log.d("DownloadManagerListener", "Загрузка ожидает")
                Download.STATE_DOWNLOADING -> Log.d(
                    "DownloadManagerListener",
                    "Загрузка в процессе"
                )

                Download.STATE_STOPPED -> Log.d(
                    "DownloadManagerListener",
                    "Загрузка приостановлена"
                )

                Download.STATE_COMPLETED -> {
                    Log.d("DownloadManagerListener", "Загрузка завершена")
                }

                Download.STATE_FAILED -> Log.d("DownloadManagerListener", "Загрузка неудачна")
                Download.STATE_REMOVING -> {
                }

                Download.STATE_RESTARTING -> {
                }
            }
        }

        override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
            Log.d("DownloadManagerListener", "onDownloadRemoved")
        }
    }

}


