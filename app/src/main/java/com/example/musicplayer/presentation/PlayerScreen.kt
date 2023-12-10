package com.example.musicplayer.presentation

import android.os.Bundle
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.audio.SystemAudioRepository
import com.google.android.horologist.audio.ui.VolumeViewModel
import com.google.android.horologist.media.ui.components.PodcastControlButtons
import com.google.android.horologist.media.ui.screens.player.DefaultMediaInfoDisplay
import com.google.android.horologist.media.ui.screens.player.PlayerScreen
import com.google.android.horologist.media.ui.state.PlayerUiController
import com.google.android.horologist.media.ui.state.PlayerUiState

class PlayerScreen : ComponentActivity() {
    @androidx.annotation.OptIn(UnstableApi::class)
    @OptIn(ExperimentalHorologistApi::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val player = ExoPlayer.Builder(this)
            .setSeekForwardIncrementMs(5000L)
            .setSeekBackIncrementMs(5000L)
            .build()
        val viewModel = MyViewModel(player)
        val volumeViewModel = createVolumeViewModel()

        setContent {
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
    }

    @OptIn(ExperimentalHorologistApi::class)
    private fun createVolumeViewModel(): VolumeViewModel {
        val audioRepository = SystemAudioRepository.fromContext(application)
        val vibrator: Vibrator = application.getSystemService(Vibrator::class.java)
        return VolumeViewModel(audioRepository, audioRepository, onCleared = {
            audioRepository.close()
        }, vibrator)
    }
}