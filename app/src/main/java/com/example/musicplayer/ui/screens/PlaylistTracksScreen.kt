package com.example.musicplayer.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberRevealState
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import com.example.musicplayer.ui.components.EmptyBox
import com.example.musicplayer.ui.components.Loading
import com.example.musicplayer.ui.components.Retry
import com.example.musicplayer.ui.components.SongCard
import com.example.musicplayer.ui.components.SwipeSongCard
import com.example.musicplayer.ui.viewModel.PlaylistTracksViewModel
import com.example.musicplayer.ui.viewModel.state.TrackListState

@OptIn(ExperimentalWearFoundationApi::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PlayListTracksScreen(
    playlistName: String,
    mediaController: MediaController,
    navController: NavController
) {
    val tracksViewModel: PlaylistTracksViewModel = viewModel(factory = PlaylistTracksViewModel.Factory)

    val songUiState = tracksViewModel.likeUiState

    val listState = rememberScalingLazyListState()

    tracksViewModel.getTracks(playlistName)

    Scaffold(
        vignette = {
            Vignette(vignettePosition = VignettePosition.Bottom)
        },
        positionIndicator = {
            PositionIndicator(listState)
        },
        timeText = { TimeText(modifier = Modifier.scrollAway(listState)) }
    ) {
        when (songUiState) {
            is TrackListState.Empty -> {
                EmptyBox("Нет добавленных треков")
            }

            is TrackListState.Loading -> {
                Loading()
            }

            is TrackListState.Error -> {
                Retry(
                    retryAction = {
                        tracksViewModel.getTracks(playlistName)
                    })
            }
            is TrackListState.Success ->
                ScalingLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = listState
                ) {
                    item {
                        ListHeader {
                            Text(text = playlistName)
                        }
                    }
                    itemsIndexed(songUiState.tracks) { index, item ->
                        SwipeSongCard(
                            index = index,
                            mediaController = mediaController,
                            list = songUiState.tracks,
                            navController = navController,
                            id = item.id,
                            title = item.title,
                            artist = item.artist,
                            img = item.imgLink,
                            onSwipe = {
                                item.id?.let {
                                    tracksViewModel.deleteTrack(playlistName, item.id)}
                            }
                        )
                    }
                }

        }
    }
}