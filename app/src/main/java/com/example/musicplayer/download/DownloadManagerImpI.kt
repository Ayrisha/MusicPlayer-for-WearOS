package com.example.musicplayer.download


import android.annotation.SuppressLint
import android.content.Context
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
import com.example.musicplayer.ui.viewModel.state.LoadTrackState
import java.io.File
import java.util.concurrent.Executors

@SuppressLint("UnsafeOptInUsageError")
class DownloadManagerImpl(
    context: Context
) {
    private val downloadManager: DownloadManager
    private val downloadCache: SimpleCache
    private val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
    var downloadCompleteListener: ((String) -> Unit)? = null

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

        downloadManager.addDownload(downloadTrackRequest)

        DownloadService.sendAddDownload(
            context,
            MediaDownloadServiceImpl::class.java,
            downloadTrackRequest,
            /* foreground= */ false
        )
    }

    fun isSongDownloaded(songId: String): LoadTrackState {
        val downloads = downloadManager.downloadIndex.getDownloads()

        if (downloads.moveToFirst()) {
            do {
                val download = downloads.download
                val mediaId = download.request.id
                if (mediaId == songId) {
                    Log.d("isSongDownloaded", "${download.percentDownloaded}")
                    val state = when (download.state) {
                        Download.STATE_COMPLETED -> LoadTrackState.Load
                        Download.STATE_QUEUED -> LoadTrackState.Progress(percent = download.percentDownloaded.toInt())
                        Download.STATE_DOWNLOADING -> LoadTrackState.Progress(percent = download.percentDownloaded.toInt())
                        else -> LoadTrackState.Unload
                    }
                    downloads.close()
                    return state
                }
            } while (downloads.moveToNext())
        }

        downloads.close()
        return LoadTrackState.Unload
    }

    private inner class DownloadManagerListener : DownloadManager.Listener {
        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: Exception?
        ) {
            when (download.state) {
                Download.STATE_QUEUED -> Log.d("DownloadManagerListener", "Загрузка ожидает")
                Download.STATE_DOWNLOADING -> {
                    Log.d(
                        "DownloadManagerListener",
                        "Загрузка в процессе"
                    )
                    downloadCompleteListener?.invoke(download.request.id)
                }

                Download.STATE_STOPPED -> {
                    Log.d(
                        "DownloadManagerListener",
                        "Загрузка приостановлена"
                    )
                }

                Download.STATE_COMPLETED -> {
                    Log.d("DownloadManagerListener", "Загрузка завершена")
                    downloadCompleteListener?.invoke(download.request.id)
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


