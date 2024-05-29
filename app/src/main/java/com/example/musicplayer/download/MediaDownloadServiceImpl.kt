package com.example.musicplayer.download

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.Scheduler
import com.example.musicplayer.MusicApplication

@androidx.annotation.OptIn(UnstableApi::class)
class MediaDownloadServiceImpl : DownloadService(1, 1000) {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    public override fun getDownloadManager(): DownloadManager {
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
            .setContentTitle("Скачивание трека")
            .setContentText("Загрузка в процессе...")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .extend( NotificationCompat.WearableExtender())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "download_channel",
            "Download Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Notifications for download progress"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}