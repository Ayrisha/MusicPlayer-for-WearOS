package com.example.musicplayer.download

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.CacheSpan
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import java.io.File

data class DownloadedTrack(
    val uri: Uri,
    val title: String,
    val artist: String
)

data class TrackMetadata(
    val title: String,
    val artist: String
)

@SuppressLint("UnsafeOptInUsageError")
@ExperimentalHorologistApi
class DownloadTracker(
    private val cache: Cache,
    private val downloadManager: DownloadManager
) {
    init {
        downloadManager.addListener(DownloadManagerListener())
    }

    private inner class DownloadManagerListener : DownloadManager.Listener {
        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: Exception?
        ) {
            when (download.state) {
                Download.STATE_QUEUED -> Log.d("DownloadManagerListener", "Загрузка ожидает")
                Download.STATE_DOWNLOADING -> Log.d("DownloadManagerListener", "Загрузка в процессе")
                Download.STATE_STOPPED -> Log.d("DownloadManagerListener", "Загрузка приостановлена")
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

    fun addDownload(url: String, id: String, context: Context) {
        Log.d("DownloadTracker", "addDownload")
        val downloadRequest = DownloadRequest.Builder(/* id = */ id, /* uri = */ Uri.parse(url)).build()
        downloadManager.addDownload(downloadRequest)
        DownloadService.sendAddDownload(
            context,
            MediaDownloadServiceImpl::class.java,
            downloadRequest,
            /* foreground= */ false
        )
    }

    fun removeDownload(downloadId: String) {
        Log.d("DownloadTracker", "removeDownload")
        downloadManager.removeDownload(downloadId)
    }

    fun getDownloadedTracks(): List<DownloadedTrack> {
        val completedDownloads = mutableListOf<Download>()
        val downloads = downloadManager.downloadIndex.getDownloads(Download.STATE_COMPLETED)

        if (downloads.moveToFirst()) {
            do {
                val download = downloads.download
                Log.d("DownloadID", download.request.uri.toString())
                completedDownloads.add(download)
            } while (downloads.moveToNext())
        }

        downloads.close()

        return completedDownloads.mapNotNull { download ->
            if (download.state == Download.STATE_COMPLETED) {
                val uri = getUriForCachedDownload(download.request)
                val metadata = getTrackMetadata(download.request)
                uri?.let {
                    DownloadedTrack(uri, metadata.title, metadata.artist)
                }
            } else {
                null
            }
        }
    }

    private fun getUriForCachedDownload(request: DownloadRequest): Uri? {
        val cacheKey = request.uri.toString()
        val cachedSpans: MutableSet<CacheSpan> = cache.getCachedSpans(cacheKey)
        if (cachedSpans.isEmpty()) return null

        val cacheSpan = cachedSpans.first()
        val file = cacheSpan.file
        return Uri.fromFile(file)
    }

    private fun getTrackMetadata(downloadRequest: DownloadRequest): TrackMetadata {
        return TrackMetadata("Title", "Artist")
    }
}
