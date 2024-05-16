package com.example.musicplayer.download

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadHelper
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.media.data.database.dao.MediaDownloadDao.Companion.DOWNLOAD_PROGRESS_START
import com.google.android.horologist.media.data.datasource.MediaDownloadLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.http.Url
import javax.annotation.Nullable


@SuppressLint("UnsafeOptInUsageError")
@ExperimentalHorologistApi
class DownloadTracker(
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
            Log.d("DownloadManagerListener", "onDownloadChanged")
        }

        override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
            Log.d("DownloadManagerListener", "onDownloadRemoved")
        }
    }

    fun addDownload(url: Uri, id: String, context: Context) {
        Log.d("DownloadTracker", "addDownload")
        val downloadRequest = DownloadRequest.Builder(/* id = */ id, /* uri = */ url).build()
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
}
