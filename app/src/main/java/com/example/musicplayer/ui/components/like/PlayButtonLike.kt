package com.example.musicplayer.ui.components.like

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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.components.auth.UserInfoScreen
import com.example.musicplayer.ui.viewModel.state.TrackListState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayButtonLike (
    navController: NavController,
    songUiState: TrackListState,
    mediaManager: MediaManager,
    pagerState: PagerState
){
    val coroutineScope  = rememberCoroutineScope()

    Button(
        modifier = Modifier.size(ButtonDefaults.SmallButtonSize),
        onClick = {
            when(songUiState){
                TrackListState.Empty -> {}
                TrackListState.Error -> {}
                TrackListState.Loading -> {}
                TrackListState.NotRegister -> {}
                is TrackListState.Success -> {
                    coroutineScope.launch {
                        mediaManager.setMediaItems(songUiState.tracks, 0)
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