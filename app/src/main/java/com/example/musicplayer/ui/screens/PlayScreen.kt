package com.example.musicplayer.ui.screens

import android.content.Context
import android.os.Build
import android.os.Vibrator
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayer.PlayerViewModel
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.audio.SystemAudioRepository
import com.google.android.horologist.audio.ui.VolumeViewModel
import com.google.android.horologist.media.ui.screens.player.PlayerScreen

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@androidx.annotation.OptIn(UnstableApi::class) @OptIn(ExperimentalHorologistApi::class)
@Composable
fun PlayScreen(context: Context, id: String) {
    val player = ExoPlayer.Builder(context).build()
    val playerViewModel = PlayerViewModel(player)
    playerViewModel.setTrack(id)
    val volumeViewModel = createVolumeViewModel(context)

    PlayerScreen(playerViewModel, volumeViewModel)
}

@OptIn(ExperimentalHorologistApi::class)
private fun createVolumeViewModel(context: Context): VolumeViewModel {
    val audioRepository = SystemAudioRepository.fromContext(context)
    val vibrator: Vibrator = context.getSystemService(Vibrator::class.java)
    return VolumeViewModel(audioRepository, audioRepository, onCleared = {
        audioRepository.close()
    }, vibrator)
}