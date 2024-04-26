package com.example.musicplayer.data.download

import android.annotation.SuppressLint
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.media.data.database.mapper.MediaDownloadEntityStatusMapper
import com.google.android.horologist.media.data.database.model.MediaDownloadEntityStatus
import com.google.android.horologist.media.data.datasource.MediaDownloadLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnsafeOptInUsageError")
@ExperimentalHorologistApi
public class DownloadManagerListener(
    private val coroutineScope: CoroutineScope,
    private val mediaDownloadLocalDataSource: MediaDownloadLocalDataSource,
    private val downloadProgressMonitor: DownloadProgressMonitor,
) : DownloadManager.Listener {

    override fun onInitialized(downloadManager: DownloadManager) {
        downloadProgressMonitor.start(downloadManager)
    }

    override fun onIdle(downloadManager: DownloadManager) {
        downloadProgressMonitor.stop()
    }

    override fun onDownloadChanged(
        downloadManager: DownloadManager,
        download: Download,
        finalException: Exception?,
    ) {
        coroutineScope.launch {
            val mediaId = download.request.id
            val status = MediaDownloadEntityStatusMapper.map(download.state)

            if (status == MediaDownloadEntityStatus.Downloaded) {
                mediaDownloadLocalDataSource.setDownloaded(mediaId)
            } else {
                mediaDownloadLocalDataSource.updateStatus(mediaId, status)
            }
        }

        downloadProgressMonitor.start(downloadManager)
    }

    override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
        coroutineScope.launch {
            val mediaId = download.request.id
            mediaDownloadLocalDataSource.delete(mediaId)
        }
    }

    internal fun onDownloadServiceCreated(downloadManager: DownloadManager) {
        downloadProgressMonitor.start(downloadManager)
    }

    internal fun onDownloadServiceDestroyed() {
        downloadProgressMonitor.stop()
    }
}