package com.example.musicplayer.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.scrollAway
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.download.DownloadTracker
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.components.SongCard
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import kotlinx.coroutines.launch

@UnstableApi @OptIn(ExperimentalHorologistApi::class)
@ExperimentalFoundationApi
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun LoadMusicScreen(
    mediaController: MediaController,
    context: Context,
    pagerState: PagerState
) {
    val downloadManagerImpl =
        (context.applicationContext as MusicApplication).container.downloadManagerImpl

    val downloadedTracks = DownloadTracker(downloadManagerImpl.getDownloadedManager()).getDownloadedTracks()

    val listState = rememberScalingLazyListState()

    val mediaManager = MediaManager(mediaController)

    val coroutineScope = rememberCoroutineScope()

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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
            flingBehavior = ScalingLazyColumnDefaults.snapFlingBehavior(
                state = listState
            )
        ) {
            item {
                ListHeader {
                    Text(text = "Скачанное")
                }
            }
            itemsIndexed(downloadedTracks) { index, item ->
                SongCard(
                    id = item.id,
                    title = item.title,
                    artist = item.artist,
                    img = item.imgLink,
                    onClick = {
                        mediaManager.setMediaItems(downloadedTracks, index = index)
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    onLongClick = { /*TODO*/ },
                    mediaController = mediaController
                )
            }
        }
    }
}