package com.example.musicplayer.data.download

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.media3.exoplayer.offline.DownloadManager
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.media.data.database.dao.MediaDownloadDao.Companion.DOWNLOAD_PROGRESS_START
import com.google.android.horologist.media.data.datasource.MediaDownloadLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnsafeOptInUsageError")
@ExperimentalHorologistApi
public class DownloadProgressMonitor(
    private val coroutineScope: CoroutineScope,
    private val mediaDownloadLocalDataSource: MediaDownloadLocalDataSource,
) {

    private val handler = Handler(Looper.getMainLooper())
    private var running = false

    fun start(downloadManager: DownloadManager) {
        running = true
        update(downloadManager)
    }

    fun stop() {
        running = false
        handler.removeCallbacksAndMessages(null)
    }

    private fun update(downloadManager: DownloadManager) {
        coroutineScope.launch {
            val downloads = mediaDownloadLocalDataSource.getAllDownloading()

            if (downloads.isNotEmpty()) {
                for (it in downloads) {
                    downloadManager.downloadIndex.getDownload(it.mediaId)?.let { download ->
                        mediaDownloadLocalDataSource.updateProgress(
                            mediaId = download.request.id,
                            progress = download.percentDownloaded
                                .coerceAtLeast(DOWNLOAD_PROGRESS_START),
                            size = download.contentLength,
                        )
                    }
                }
            } else {
                stop()
            }
        }

        if (running) {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({ update(downloadManager) }, UPDATE_INTERVAL_MILLIS)
        }
    }

    private companion object {
        const val UPDATE_INTERVAL_MILLIS = 1000L
    }
}