package com.example.musicplayer.ui.components.load

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.viewModel.state.DownloadTrackState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayButtonLoad (
    downloadTrackState: DownloadTrackState,
    mediaManager: MediaManager,
    pagerState: PagerState
){
    val coroutineScope  = rememberCoroutineScope()

    Button(
        modifier = Modifier.size(ButtonDefaults.SmallButtonSize),
        onClick = {
            when(downloadTrackState) {
                is DownloadTrackState.Empty -> {}
                is DownloadTrackState.NotEmpty -> {
                    coroutineScope.launch {
                        mediaManager.setMediaItems(downloadTrackState.trackList, 0)
                        pagerState.animateScrollToPage(1)
                    }
                }
            }
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(48, 79, 254))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "item image",
                tint = Color.White
            )
        }
    }
}