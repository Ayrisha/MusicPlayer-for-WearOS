package com.example.musicplayer.ui.components.playlist

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import com.example.musicplayer.R
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.viewModel.PlayListViewModel
import com.example.musicplayer.ui.viewModel.state.TrackListState
import kotlinx.coroutines.launch

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayButtonPlaylist (
    navController: NavController,
    songUiState: TrackListState,
    mediaManager: MediaManager,
    pagerState: PagerState,
    playlistName: String
){
    val coroutineScope  = rememberCoroutineScope()

    val playListViewModel: PlayListViewModel = viewModel(factory = PlayListViewModel.Factory)

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.size(ButtonDefaults.SmallButtonSize),
            onClick = {
                when (songUiState) {
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

        Spacer(modifier = Modifier.padding(5.dp))

        Button(
            modifier = Modifier.size(ButtonDefaults.SmallButtonSize),
            onClick = {
                coroutineScope.launch {
                    playListViewModel.deletePlaylist(playlistName)
                    navController.popBackStack()
                }
                      },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(48, 79, 254))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_playlist_remove_24),
                    contentDescription = "item image",
                    tint = Color.White
                )
            }
        }
    }
}