package com.example.musicplayer.data.download

import android.app.PendingIntent
import androidx.core.app.ShareCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.workmanager.WorkManagerScheduler
import com.example.musicplayer.R
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.media.data.service.download.DownloadManagerListener
import com.google.android.horologist.media.data.service.download.MediaDownloadService

@androidx.annotation.OptIn(UnstableApi::class) @OptIn(ExperimentalHorologistApi::class)
class MediaDownloadServiceImpl : MediaDownloadService(
    MEDIA_DOWNLOAD_FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    MEDIA_DOWNLOAD_CHANNEL_ID,
    MEDIA_DOWNLOAD_CHANNEL_NAME,
    MEDIA_DOWNLOAD_CHANNEL_DESCRIPTION_NOT_PROVIDED,
    MEDIA_DOWNLOAD_NOTIFICATION_ICON,
) {

    lateinit var downloadManagerParam: DownloadManager

    lateinit var workManagerSchedulerParam: WorkManagerScheduler

    lateinit var downloadNotificationHelperParam: DownloadNotificationHelper

    lateinit var intentBuilder: ShareCompat.IntentBuilder

    lateinit var downloadManagerListenerParam: DownloadManagerListener

    override fun getDownloadManager(): DownloadManager = downloadManagerParam

    override val downloadManagerListener: DownloadManagerListener
        get() = downloadManagerListenerParam

    override val workManagerScheduler: WorkManagerScheduler
        get() = workManagerSchedulerParam

    override val downloadIntent: PendingIntent
        get() {
            TODO()
        }

    override val downloadNotificationHelper: DownloadNotificationHelper
        get() = downloadNotificationHelperParam

    companion object {
        private const val MEDIA_DOWNLOAD_FOREGROUND_NOTIFICATION_ID = 1
        const val MEDIA_DOWNLOAD_CHANNEL_ID = "download_channel"
        private val MEDIA_DOWNLOAD_CHANNEL_NAME = R.string.download
        private const val MEDIA_DOWNLOAD_CHANNEL_DESCRIPTION_NOT_PROVIDED = 0
        private val MEDIA_DOWNLOAD_NOTIFICATION_ICON = R.drawable.baseline_download_24
    }
}