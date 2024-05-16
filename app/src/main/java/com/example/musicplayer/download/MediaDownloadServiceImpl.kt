package com.example.musicplayer.download

import android.annotation.SuppressLint
import android.app.Notification
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.Scheduler
import com.example.musicplayer.MusicApplication

@androidx.annotation.OptIn(UnstableApi::class)
class MediaDownloadServiceImpl : DownloadService(1) {
    @SuppressLint("ServiceCast")
    override fun getDownloadManager(): DownloadManager {
        return (applicationContext as MusicApplication).container.downloadManager
    }
    override fun getScheduler(): Scheduler? {
        return null
    }

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int
    ): Notification {
        return NotificationCompat.Builder(this, "download_channel")
            .setContentTitle("Downloading Music")
            .setContentText("Downloading in progress")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
    }
}