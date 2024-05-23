package com.example.musicplayer.ui.components.search

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyListScope
import androidx.wear.compose.foundation.lazy.itemsIndexed
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.components.EmptyBox
import com.example.musicplayer.ui.components.Loading
import com.example.musicplayer.ui.components.NotRegister
import com.example.musicplayer.ui.components.Retry
import com.example.musicplayer.ui.components.SongCard
import com.example.musicplayer.ui.viewModel.SearchViewModel
import com.example.musicplayer.ui.viewModel.state.TrackUiState


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
fun ScalingLazyListScope.trackUiStateContent(
    songUiState: TrackUiState,
    mediaManager: MediaManager,
    trackViewModel: SearchViewModel,
    navController: NavController,
    onClick: () -> Unit
) {
    when (songUiState) {
        is TrackUiState.Start -> {
            itemsIndexed(songUiState.trackPopular) { index, item ->
                SongCard(
                    id = item.id,
                    title = item.title,
                    artist = item.artist,
                    img = "http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/image/${item.imgLink}.png",
                    onClick = {
                        mediaManager.setMediaItems(songUiState.trackPopular, index)
                        onClick()
                    },
                    onLongClick = {
                        navController.navigate("song_info/${item.title}/${item.artist}/${item.imgLink}/${item.id}")
                    },
                    mediaController = mediaManager.getMediaController()
                )
            }
        }
        is TrackUiState.Success -> {
            itemsIndexed(songUiState.trackSearches) { index, item ->
                SongCard(
                    id = item.id,
                    title = item.title,
                    artist = item.artist,
                    img = "http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/image/${item.imgLink}.png",
                    onClick = {
                        mediaManager.setMediaItems(songUiState.trackSearches, index)
                        onClick()
                    },
                    onLongClick = {
                        navController.navigate("song_info/${item.title}/${item.artist}/${item.imgLink}/${item.id}")
                    },
                    mediaController = mediaManager.getMediaController()
                )
            }
        }

        is TrackUiState.NotRegister -> item {
            NotRegister(
                registerAction = {
                    navController.navigate("auth")
                }
            )
        }
        is TrackUiState.Empty -> item {
            EmptyBox("По вашему запросу ничего не найдено")
        }
        is TrackUiState.Loading -> item {
            Loading()
        }
        is TrackUiState.Error -> item {
            Retry {
                    trackViewModel.popularTrack()
            }
        }
    }
}