package com.example.musicplayer.ui.screens

import android.content.Context
import android.os.Build
import android.os.Vibrator
import androidx.annotation.RequiresExtension
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import com.example.musicplayer.R
import com.example.musicplayer.ui.components.PlayListChip
import com.example.musicplayer.ui.viewModel.PlayerViewModel
import com.example.musicplayer.ui.viewModel.state.LikeState
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.audio.SystemAudioRepository
import com.google.android.horologist.audio.ui.VolumeViewModel
import com.google.android.horologist.media.ui.screens.player.PlayerScreen

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalHorologistApi::class)
@Composable
fun  PlayScreen(
    navController: NavController,
    controller: MediaController,
    playerViewModel: PlayerViewModel,
) {
    val context = LocalContext.current
    val volumeViewModel = createVolumeViewModel(context)

    val likeState: LikeState by mutableStateOf(playerViewModel.likeState)

    PlayerScreen(
        playerViewModel = playerViewModel,
        volumeViewModel = volumeViewModel,
        buttons = {
            IconButton(
                onClick = {
                }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_download_24),
                    tint = Color.White,
                    contentDescription = "загрузка"
                )
            }
            IconButton(
                onClick = {
                    when(likeState) {
                        LikeState.Dislike -> {
                            playerViewModel.setLike(controller.currentMediaItem?.mediaId.toString())
                        }

                        LikeState.Like -> {
                            playerViewModel.deleteLike(controller.currentMediaItem?.mediaId.toString())
                        }
                    }
                }
                ) {
                when(likeState) {
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
            IconButton(
                onClick = {
                    navController.navigate("playlists/${controller.currentMediaItem?.mediaId.toString()}")
                }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_playlist_add_24),
                    tint = Color.White,
                    contentDescription = "плейлист"
                )
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