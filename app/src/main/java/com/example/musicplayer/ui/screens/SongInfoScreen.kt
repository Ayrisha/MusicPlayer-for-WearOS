package com.example.musicplayer.ui.screens

import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListAnchorType
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import androidx.wear.compose.material.dialog.Dialog
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.data.model.Track
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.components.SongCard
import com.example.musicplayer.ui.viewModel.DownloadViewModel
import com.example.musicplayer.ui.viewModel.LikeViewModel
import com.example.musicplayer.ui.viewModel.PlaylistTracksViewModel
import com.example.musicplayer.ui.viewModel.state.LikeState
import com.example.musicplayer.ui.viewModel.state.LoadTrackState
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.rotaryinput.ScalingLazyColumnRotaryScrollAdapter
import com.google.android.horologist.compose.rotaryinput.rotaryWithSnap
import com.google.android.horologist.media.model.Playlist
import kotlinx.coroutines.launch


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(
    ExperimentalWearFoundationApi::class, ExperimentalHorologistApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun SongInfoScreen(
    id: String?,
    title: String,
    artist: String?,
    img: String?,
    mediaController: MediaController,
    navController: NavController,
    pagerState: PagerState,
    playlist: String? = null
) {
    val mediaManager = MediaManager(mediaController)

    val context = LocalContext.current

    val likeViewModel: LikeViewModel = viewModel(factory = LikeViewModel.Factory)

    val downloadViewModel: DownloadViewModel = viewModel(factory = DownloadViewModel.Factory)

    val downloadTrackState: LoadTrackState by mutableStateOf(downloadViewModel.loadState)

    val likeState: LikeState by mutableStateOf(likeViewModel.likeState)

    val tracksViewModel: PlaylistTracksViewModel =
        viewModel(factory = PlaylistTracksViewModel.Factory)

    val listState = rememberScalingLazyListState()

    val coroutineScope = rememberCoroutineScope()

    val focusRequester = rememberActiveFocusRequester()

    val rotaryScrollAdapter = remember { ScalingLazyColumnRotaryScrollAdapter(listState) }

    val downloadManagerImpl =
        (context.applicationContext as MusicApplication).container.downloadManagerImpl

    var showDialog by remember { mutableStateOf(false) }

    val fileSize by downloadViewModel.fileSize.observeAsState(0L)

    fun getAvailableStorageSpaceInMB(): Long {
        val statFs = StatFs(Environment.getExternalStorageDirectory().absolutePath)
        val availableBlocks = statFs.availableBlocksLong
        val blockSize = statFs.blockSizeLong
        val availableSpaceInBytes = availableBlocks * blockSize
        return availableSpaceInBytes / (1024 * 1024 * 1024)
    }

    val availableSize = getAvailableStorageSpaceInMB()

    if (id != null) {
        likeViewModel.checkLikeTrack(id)
        downloadViewModel.checkLoadTrack(id, downloadManagerImpl)
    }

    Scaffold(
        positionIndicator = { PositionIndicator(listState) }
    ) {
        ScalingLazyColumn(
            modifier = Modifier
                .onRotaryScrollEvent {
                    coroutineScope.launch {
                        listState.scrollBy(it.verticalScrollPixels)
                    }
                    true
                }
                .rotaryWithSnap(rotaryScrollAdapter = rotaryScrollAdapter)
                .focusRequester(focusRequester)
                .focusable(),
            anchorType = ScalingLazyListAnchorType.ItemCenter,
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { 
                ListHeader {
                    Text(text = "Опции")
                }
            }

            item {
                SongCard(
                    id = id,
                    title = title,
                    artist = artist,
                    img = img,
                    onClick = {  },
                    mediaController = mediaController
                )
            }

            item {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(bottom = 30.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color(0xFF1C1B1F)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        SongInfoButton(
                            imageVector = Icons.Filled.PlayArrow,
                            text = "Проиграть",
                            onClick = {
                                coroutineScope.launch {
                                    mediaManager.sedMediaItem(
                                        Track(
                                            id = id,
                                            title = title,
                                            artist = artist,
                                            imgLink = img
                                        )
                                    )
                                    pagerState.animateScrollToPage(1)
                                }
                            })

                        Divider(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            color = Color.White.copy(alpha = 0.2F),
                            thickness = 0.5.dp
                        )

                        SongInfoButton(
                            imageVector = when (likeState) {
                                LikeState.Like -> Icons.Filled.Favorite
                                LikeState.Dislike -> Icons.Filled.FavoriteBorder
                                LikeState.NotConnection -> ImageVector.vectorResource(R.drawable.outline_cloud_off_24)
                            },
                            text = when (likeState) {
                                LikeState.Like -> "Не нравится"
                                LikeState.Dislike -> "Нравится"
                                LikeState.NotConnection -> "Нет подключения"
                            },
                            onClick = {
                                when (likeState) {
                                    LikeState.Dislike -> {
                                        if (id != null) {
                                            likeViewModel.setLike(id)
                                            navController.popBackStack()
                                        }
                                    }

                                    LikeState.Like -> {
                                        if (id != null) {
                                            likeViewModel.deleteLike(id)
                                            navController.popBackStack()
                                        }
                                    }

                                    LikeState.NotConnection -> TODO()
                                }
                            })

                        Divider(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            color = Color.White.copy(alpha = 0.2F),
                            thickness = 0.5.dp
                        )

                        SongInfoButton(
                            imageVector = when (downloadTrackState) {
                                LoadTrackState.Load -> ImageVector.vectorResource(id = R.drawable.outline_delete_24)
                                LoadTrackState.Unload -> ImageVector.vectorResource(id = R.drawable.baseline_download_24)
                                is LoadTrackState.Progress -> ImageVector.vectorResource(id = R.drawable.tray_arrow_down)
                            },
                            text = when (downloadTrackState) {
                                LoadTrackState.Load -> "Удалить c устройства"
                                LoadTrackState.Unload -> "Скачать"
                                is LoadTrackState.Progress -> "Загрузка..."
                            },
                            onClick = {
                                when (downloadTrackState) {
                                    LoadTrackState.Load -> {
                                        if (id != null) {
                                            downloadManagerImpl.removeDownload(
                                                context = context,
                                                downloadId = id
                                            )
                                            navController.popBackStack()
                                        }
                                    }
                                    LoadTrackState.Unload -> {
                                        downloadViewModel.getFileSizeOfUrl("http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/music/${id}.mp3")
                                        showDialog = true
                                    }

                                    is LoadTrackState.Progress -> TODO()
                                }
                            })


                        Divider(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            color = Color.White.copy(alpha = 0.2F),
                            thickness = 0.5.dp
                        )

                        SongInfoButton(
                            imageVector = if (playlist.isNullOrEmpty()){
                                ImageVector.vectorResource(id = R.drawable.baseline_playlist_add_24)
                            }
                            else{
                                ImageVector.vectorResource(id = R.drawable.baseline_horizontal_rule_24)
                                },
                            text = if (playlist.isNullOrEmpty()){
                                "Добавить в плейлист"
                            }
                            else{
                                "Удалить из плейлиста"
                            },
                            onClick = {
                                if (playlist.isNullOrEmpty()){
                                    navController.navigate(Routes.PlaylistScreen + "/${id}")
                                }
                                else{
                                    if (id != null) {
                                        tracksViewModel.deleteTrack(playlist, id)
                                        navController.popBackStack()
                                    }
                                }
                            }
                        )
                }
            }
        }
    }

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
                androidx.compose.material3.Text(
                    text = "Скачать песню",
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            },
            message = {
                androidx.compose.material3.Text(
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
                        onClick = {
                            showDialog = false
                                  },
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
                            if (id != null) {
                                if (artist != null) {
                                    downloadManagerImpl.addDownload(
                                        url = "http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/music/${id}.mp3",
                                        id = id,
                                        title = title,
                                        artist = artist,
                                        imgUrl = "http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/image/${id}.png",
                                        context = context
                                    )
                                }
                            }
                            showDialog = false
                        },
                        colors = ChipDefaults.primaryChipColors(),
                    )
                }
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            val vibrator: Vibrator = context.getSystemService(Vibrator::class.java)
            val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            vibrator.vibrate(effect)
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun SongInfoButton(
    imageVector: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Chip(
        modifier = Modifier
            .fillMaxSize(),
        onClick = {
            onClick()
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = Color.Transparent,
        ),
        border = ChipDefaults.chipBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(22.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = imageVector,
                contentDescription = "Проиграть",
                tint = Color.White
            )
            Text(
                text = text,
                color = Color.White
            )
        }
    }
}
