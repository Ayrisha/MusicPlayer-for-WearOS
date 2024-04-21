package com.example.musicplayer.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayer.ui.viewModel.PlayerViewModel
import com.example.musicplayer.ui.screens.createVolumeViewModel
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.audio.ui.VolumeViewModel

object ExoPlayerObject {
    private lateinit var player: ExoPlayer

    @SuppressLint("UnsafeOptInUsageError")
    private lateinit var playerViewModel: PlayerViewModel

    @kotlin.OptIn(ExperimentalHorologistApi::class)
    private lateinit var volumeViewModel: VolumeViewModel

    @OptIn(UnstableApi::class) @kotlin.OptIn(ExperimentalHorologistApi::class)
    fun init(context: Context) {
        player = ExoPlayer.Builder(context).build()
        playerViewModel = PlayerViewModel(player)
        volumeViewModel = createVolumeViewModel(context)
    }
    @SuppressLint("UnsafeOptInUsageError")
    fun getPlayerViewModel(): PlayerViewModel {
        return playerViewModel
    }
    @kotlin.OptIn(ExperimentalHorologistApi::class)
    fun getVolumeViewModel(): VolumeViewModel {
        return volumeViewModel
    }

    fun destroy(){
        player.release()
    }
}