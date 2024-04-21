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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.media3.common.util.UnstableApi
import androidx.wear.compose.material.ButtonDefaults
import com.example.musicplayer.R
import com.example.musicplayer.data.ExoPlayerObject
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.audio.SystemAudioRepository
import com.google.android.horologist.audio.ui.VolumeViewModel
import com.google.android.horologist.media.ui.components.PodcastControlButtons
import com.google.android.horologist.media.ui.components.controls.MediaButtonDefaults
import com.google.android.horologist.media.ui.screens.player.PlayerScreen
import com.google.android.horologist.media.ui.state.PlayerUiController
import com.google.android.horologist.media.ui.state.PlayerUiState

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalHorologistApi::class)
@Composable
fun PlayScreen(
    id: String,
    title: String? = "No name",
    artist: String? = "No name"
) {
    var likeState by remember { mutableStateOf(false) }
    ExoPlayerObject.getPlayerViewModel().setTrack(id, title.toString(), artist.toString())
    PlayerScreen(
        playerViewModel = ExoPlayerObject.getPlayerViewModel(),
        volumeViewModel = ExoPlayerObject.getVolumeViewModel(),
        buttons = {
            IconButton(
                onClick = { }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_download_24),
                    tint = Color.White,
                    contentDescription = "Информация о приложении"
                )
            }
            IconButton(
                onClick = { likeState = !likeState }) {
                if (likeState) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        tint = Color.White,
                        contentDescription = "Информация о приложении"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        tint = Color.White,
                        contentDescription = "Информация о приложении"
                    )
                }
            }
            IconButton(
                onClick = { }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_playlist_add_24),
                    tint = Color.White,
                    contentDescription = "Информация о приложении"
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