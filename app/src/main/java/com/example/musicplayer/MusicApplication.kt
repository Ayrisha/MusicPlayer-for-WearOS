package com.example.musicplayer

import android.app.Application
import androidx.annotation.OptIn
import androidx.compose.runtime.rememberCoroutineScope
import androidx.media3.common.util.UnstableApi

class MusicApplication: Application() {
    lateinit var container: AppContainer

    @OptIn(UnstableApi::class) override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}