package com.example.musicplayer

import androidx.media3.common.util.UnstableApi
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.media.data.service.download.MediaDownloadService

@androidx.annotation.OptIn(UnstableApi::class) @OptIn(ExperimentalHorologistApi::class)
abstract class MediaDownloadServiceImpl : MediaDownloadService(FOREGROUND_NOTIFICATION_ID_NONE,
    0, "0", 0,
    0, 0) {
}