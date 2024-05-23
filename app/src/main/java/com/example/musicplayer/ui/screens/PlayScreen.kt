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
import com.example.musicplayer.download.DownloadTracker
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
    navController: NavController,
    controller: MediaController,
    playerViewModel: PlayerViewModel,
) {
    val context = LocalContext.current

    val volumeViewModel = createVolumeViewModel(context)

    val likeState: LikeState by mutableStateOf(playerViewModel.likeState)

    val loadState: LoadTrackState by mutableStateOf(playerViewModel.loadState)

    val downloadManager =
        (context.applicationContext as MusicApplication).container.downloadManagerImpl.getDownloadedManager()

    val downloadTracker = DownloadTracker(downloadManager)

    val fileSize by playerViewModel.fileSize.observeAsState(0L)

    var showDialog by remember { mutableStateOf(false) }

    fun getAvailableStorageSpaceInMB(): Long {
        val statFs = StatFs(Environment.getExternalStorageDirectory().absolutePath)
        val availableBlocks = statFs.availableBlocksLong
        val blockSize = statFs.blockSizeLong
        val availableSpaceInBytes = availableBlocks * blockSize
        return availableSpaceInBytes / (1024 * 1024 * 1024)
    }

    val availableSize = getAvailableStorageSpaceInMB()

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
                    controller.currentMediaItem?.let {
                        when(loadState){
                            LoadTrackState.Load -> TODO()
                            LoadTrackState.Unload -> {
                                playerViewModel.getFileSizeOfUrl("http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/music/${it.mediaId}.mp3")
                                showDialog = true
                            }
                        }
                    }
                }
            ) {
                when(loadState){
                    LoadTrackState.Load -> {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_cloud_off_24),
                            tint = Color.White,
                            contentDescription = "удаление"
                        )
                    }
                    LoadTrackState.Unload -> {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_download_24),
                            tint = Color.White,
                            contentDescription = "загрузка"
                        )
                    }
                }
            }
            IconButton(
                onClick = {
                    controller.currentMediaItem?.let {
                        when (likeState) {
                            LikeState.Dislike -> {
                                playerViewModel.setLike(controller.currentMediaItem?.mediaId.toString())
                            }

                            LikeState.Like -> {
                                playerViewModel.deleteLike(controller.currentMediaItem?.mediaId.toString())
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
            IconButton(
                onClick = {
                    controller.currentMediaItem?.let {
                        navController.navigate("playlists/${controller.currentMediaItem?.mediaId.toString()}")
                    }
                }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_playlist_add_24),
                    tint = Color.White,
                    contentDescription = "плейлист"
                )
            }
        })

    Dialog(showDialog = showDialog, onDismissRequest = { showDialog = false }) {
        Alert(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
            contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 24.dp, bottom = 52.dp),
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_download_24),
                    contentDescription = "airplane",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .wrapContentSize(align = Alignment.Center),
                )
            },
            title = {
                Text(
                    text = "Скачать песню",
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            },
            message = {
                Text(
                    text = "Этот трек займет около $fileSize Мб свободного места. Всего свободного места $availableSize Гб. Вы хотите продолжить?",
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            },
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Chip(
                        label = { },
                        icon = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "No",
                                    tint = Color.White
                                )
                            }
                        },
                        contentPadding = ChipDefaults.ContentPadding,
                        onClick = { showDialog = false },
                        colors = ChipDefaults.secondaryChipColors(),
                    )

                    Chip(
                        label = { },
                        icon = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Yes",
                                    tint = Color.Black
                                )
                            }
                        },
                        contentPadding = ChipDefaults.ContentPadding,
                        onClick = {
                            controller.currentMediaItem?.let { it1 ->
                                downloadTracker.addDownload(
                                    url = "http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/music/${controller.currentMediaItem?.mediaId}.mp3",
                                    id = it1.mediaId,
                                    title = it1.mediaMetadata.title.toString(),
                                    artist = it1.mediaMetadata.artist.toString(),
                                    imgUrl = "http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/image/${controller.currentMediaItem?.mediaId}.png",
                                    context = context
                                )
                            }
                        },
                        colors = ChipDefaults.primaryChipColors(),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalHorologistApi::class)
fun createVolumeViewModel(context: Context): VolumeViewModel {
    val audioRepository = SystemAudioRepository.fromContext(context)
    val vibrator: Vibrator = context.getSystemService(Vibrator::class.java)
    return VolumeViewModel(audioRepository, audioRepository, onCleared = {
        audioRepository.close()
    }, vibrator)
}
