package com.example.musicplayer.ui.screens

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.dialog.Alert
import androidx.wear.compose.material.dialog.Dialog
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.ui.viewModel.PlayerViewModel
import com.example.musicplayer.ui.viewModel.state.LikeState
import com.example.musicplayer.ui.viewModel.state.LoadTrackState
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.audio.SystemAudioRepository
import com.google.android.horologist.audio.ui.VolumeViewModel
import com.google.android.horologist.media.ui.screens.player.PlayerScreen


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalHorologistApi::class)
@Composable
fun PlayScreen(
    playerViewModel: PlayerViewModel,
    navController: NavController,
    mediaController: MediaController
) {
    val context = LocalContext.current

    val volumeViewModel = createVolumeViewModel(context)

    val likeState: LikeState by mutableStateOf(playerViewModel.likeState)

    DisposableEffect(Unit) {
        onDispose {
            val vibrator: Vibrator = context.getSystemService(Vibrator::class.java)
            val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            vibrator.vibrate(effect)
        }
    }

    PlayerScreen(
        playerViewModel = playerViewModel,
        volumeViewModel = volumeViewModel,
        buttons = {
            IconButton(
                onClick = {
                    mediaController.currentMediaItem?.let {
                        when (likeState) {
                            LikeState.Dislike -> {
                                playerViewModel.setLike(mediaController.currentMediaItem?.mediaId.toString())
                            }

                            LikeState.Like -> {
                                playerViewModel.deleteLike(mediaController.currentMediaItem?.mediaId.toString())
                            }
                        }
                    }
                }
            ) {
                when (likeState) {
                    LikeState.Like -> {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            tint = Color.White,
                            contentDescription = "лайк"
                        )
                    }

                    LikeState.Dislike -> {
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            tint = Color.White,
                            contentDescription = "лайк"
                        )
                    }
                }
            }
        })
}

@OptIn(ExperimentalHorologistApi::class)
fun createVolumeViewModel(context: Context): VolumeViewModel {
    val audioRepository = SystemAudioRepository.fromContext(context)
    val vibrator: Vibrator = context.getSystemService(Vibrator::class.java)
    return VolumeViewModel(audioRepository, audioRepository, onCleared = {
        audioRepository.close()
    }, vibrator)
}
