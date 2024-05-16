package com.example.musicplayer.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import com.example.musicplayer.ui.components.EmptyBox
import com.example.musicplayer.ui.components.Loading
import com.example.musicplayer.ui.components.PlayListChip
import com.example.musicplayer.ui.components.Retry
import com.example.musicplayer.ui.viewModel.PlayListViewModel
import com.example.musicplayer.ui.viewModel.state.PlayListUiState

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PlaylistScreen(
    navController: NavController,
    trackId: String? = null
) {
    val playListViewModel: PlayListViewModel = viewModel(factory = PlayListViewModel.Factory)

    val listState = rememberScalingLazyListState()

    val playListUiState = playListViewModel.playListUiState

    playListViewModel.getPlaylists()

    Scaffold(
        vignette = {
            Vignette(vignettePosition = VignettePosition.Bottom)
        },
        positionIndicator = {
            PositionIndicator(listState)
        },
        timeText = { TimeText(
            modifier = Modifier.scrollAway(listState)) }
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
                Button(
                    modifier = Modifier
                        .size(ButtonDefaults.SmallButtonSize),
                    onClick = {
                        navController.navigate("addplaylist")
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(48, 79, 254)
                    )
                ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "item image",
                                tint = Color.White
                            )
                        }
                }

            }
            item {
                ListHeader {
                    Text(text = "Плейлисты")
                }
            }
            when (playListUiState) {
                is PlayListUiState.Empty -> item { EmptyBox(text = "Нет плейлистов") }
                is PlayListUiState.Error -> item {
                    Retry {
                        playListViewModel.getPlaylists()
                    }
                }
                is PlayListUiState.Loading -> item{
                    Loading()
                }
                is PlayListUiState.Success ->
                    itemsIndexed(playListUiState.plaLists, key = { _, item -> item.title }) { _, item ->
                        PlayListChip(
                            text = item.title,
                            navController = navController,
                            trackId = trackId,
                            onSwipe = {
                                playListViewModel.deletePlaylist(item.title)
                            })
                }
            }
        }
    }
}