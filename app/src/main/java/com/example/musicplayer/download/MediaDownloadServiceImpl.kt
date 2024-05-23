package com.example.musicplayer.download

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.Scheduler
import com.example.musicplayer.MusicApplication

@androidx.annotation.OptIn(UnstableApi::class)
class MediaDownloadServiceImpl : DownloadService(1) {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    @SuppressLint("ServiceCast")
    override fun getDownloadManager(): DownloadManager {
        return (applicationContext as MusicApplication).container.downloadManagerImpl.getDownloadedManager()
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
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "download_channel",
            "Download Notifications",
            NotificationManager.IMPORTANCE_LOW
        )
        channel.description = "Notifications for download progress"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}