package com.example.musicplayer.ui.components.search

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyListScope
import androidx.wear.compose.foundation.lazy.itemsIndexed
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.components.EmptyBox
import com.example.musicplayer.ui.components.Loading
import com.example.musicplayer.ui.components.Retry
import com.example.musicplayer.ui.components.SongCard
import com.example.musicplayer.ui.viewModel.SearchViewModel
import com.example.musicplayer.ui.viewModel.state.TrackUiState

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
fun ScalingLazyListScope.trackUiStateContent(
    songUiState: TrackUiState,
    mediaManager: MediaManager,
    navController: NavController,
    searchQuery: String?,
    trackViewModel: SearchViewModel
) {
    when (songUiState) {
        is TrackUiState.Start -> {
            itemsIndexed(songUiState.trackPopular) { index, item ->
                SongCard(
                    title = item.title,
                    artist = item.artist,
                    img = item.imgLink,
                    onClick = {
                        mediaManager.setMediaItems(songUiState.trackPopular, index)
                        navController.navigate("play_screen")
                    }
                )
            }
        }
        is TrackUiState.Success -> {
            itemsIndexed(songUiState.trackSearches) { index, item ->
                SongCard(
                    title = item.title,
                    artist = item.artist,
                    img = item.imgLink,
                    onClick = {
                        mediaManager.setMediaItems(songUiState.trackSearches, index)
                        navController.navigate("play_screen")
                    }
                )
            }
        }
        is TrackUiState.Empty -> item {
            EmptyBox("По вашему запросу ничего не найдено")
        }
        is TrackUiState.Loading -> item {
            Loading()
        }
        is TrackUiState.Error -> item {
            Retry {
                if (searchQuery != null) {
                    trackViewModel.searchTrack(searchQuery)
                } else {
                    trackViewModel.popularTrack()
                }
            }
        }
    }
}