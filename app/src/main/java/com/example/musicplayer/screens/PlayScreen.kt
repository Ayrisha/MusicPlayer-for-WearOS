package com.example.musicplayer.screens

import android.content.Context
import android.os.Vibrator
import androidx.compose.runtime.Composable
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicplayer.MyViewModel
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.audio.SystemAudioRepository
import com.google.android.horologist.audio.ui.VolumeViewModel
import com.google.android.horologist.media.ui.components.PodcastControlButtons
import com.google.android.horologist.media.ui.screens.player.DefaultMediaInfoDisplay
import com.google.android.horologist.media.ui.screens.player.PlayerScreen
import com.google.android.horologist.media.ui.state.PlayerUiController
import com.google.android.horologist.media.ui.state.PlayerUiState

@androidx.annotation.OptIn(UnstableApi::class) @OptIn(ExperimentalHorologistApi::class)
@Composable
fun PlayScreen(context: Context) {
    val player = ExoPlayer.Builder(context)
        .setSeekForwardIncrementMs(5000L)
        .setSeekBackIncrementMs(5000L)
        .build()
    val viewModel = MyViewModel(player)
    val volumeViewModel = createVolumeViewModel(context)

    PlayerScreen(
        playerViewModel = viewModel,
        volumeViewModel = volumeViewModel,
        mediaDisplay = { playerUiState: PlayerUiState ->
            DefaultMediaInfoDisplay(playerUiState)
        },
        controlButtons = { playerUIController: PlayerUiController,
                           playerUiState: PlayerUiState ->
            PodcastControlButtons(
                playerController = playerUIController,
                playerUiState = playerUiState
            )
        },
        buttons = {}
    )
}

@OptIn(ExperimentalHorologistApi::class)
private fun createVolumeViewModel(context: Context): VolumeViewModel {
    val audioRepository = SystemAudioRepository.fromContext(context)
    val vibrator: Vibrator = context.getSystemService(Vibrator::class.java)
    return VolumeViewModel(audioRepository, audioRepository, onCleared = {
        audioRepository.close()
    }, vibrator)
}