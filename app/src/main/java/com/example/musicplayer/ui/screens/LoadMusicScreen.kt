package com.example.musicplayer.ui.screens

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.scrollAway
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.components.EmptyBox
import com.example.musicplayer.ui.components.SongCard
import com.example.musicplayer.ui.components.load.PlayButtonLoad
import com.example.musicplayer.ui.viewModel.DownloadViewModel
import com.example.musicplayer.ui.viewModel.state.DownloadTrackState
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.rotaryinput.ScalingLazyColumnRotaryScrollAdapter
import com.google.android.horologist.compose.rotaryinput.rotaryWithSnap
import kotlinx.coroutines.launch

@OptIn(ExperimentalHorologistApi::class, ExperimentalWearFoundationApi::class)
@ExperimentalFoundationApi
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun LoadMusicScreen(
    mediaController: MediaController,
    context: Context,
    pagerState: PagerState,
    navController: NavController
) {
    val downloadViewModel: DownloadViewModel = viewModel(factory = DownloadViewModel.Factory)

    val downloadTrackState = downloadViewModel.downloadTrackState

    val listState = rememberScalingLazyListState()

    val mediaManager = MediaManager(mediaController)

    val coroutineScope = rememberCoroutineScope()

    val rotaryScrollAdapter = ScalingLazyColumnRotaryScrollAdapter(listState)

    val focusRequester = rememberActiveFocusRequester()

    downloadViewModel.getDownloadedTracks()

    DisposableEffect(Unit) {
        onDispose {
            val vibrator: Vibrator = context.getSystemService(Vibrator::class.java)
            val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            vibrator.vibrate(effect)
        }
    }

    Scaffold(
        positionIndicator = {
            PositionIndicator(listState)
        },
        timeText = { TimeText(modifier = Modifier.scrollAway(listState)) }
    ) {
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .onRotaryScrollEvent {
                    coroutineScope.launch {
                        listState.scrollBy(it.verticalScrollPixels)
                        listState.animateScrollBy(0f)
                    }
                    true
                }
                .rotaryWithSnap(rotaryScrollAdapter = rotaryScrollAdapter)
                .focusRequester(focusRequester)
                .focusable(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                PlayButtonLoad(
                    downloadTrackState = downloadTrackState,
                    mediaManager = mediaManager,
                    pagerState = pagerState
                )
            }
            item {
                ListHeader {
                    Text(text = "Скачанное")
                }
            }
            when(downloadTrackState) {
                is DownloadTrackState.Empty -> item{
                    EmptyBox(text = "Нет загруженных песен")
                }
                is DownloadTrackState.NotEmpty -> itemsIndexed(downloadTrackState.trackList){
                        index, item ->
                    val image = Uri.encode(item.imgLink)
                    SongCard(
                        id = item.id,
                        title = item.title,
                        artist = item.artist,
                        img = item.imgLink,
                        onClick = {
                            navController.navigate(Routes.SongInfoScreen + "/${item.title}/${item.artist}/${item.id}/${image}")
                        },
                        mediaController = mediaManager.getMediaController()
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