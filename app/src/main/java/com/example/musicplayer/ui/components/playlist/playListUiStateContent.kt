package com.example.musicplayer.ui.components.playlist

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyListScope
import androidx.wear.compose.foundation.lazy.itemsIndexed
import com.example.musicplayer.ui.components.EmptyBox
import com.example.musicplayer.ui.components.Loading
import com.example.musicplayer.ui.components.Retry
import com.example.musicplayer.ui.viewModel.PlayListViewModel
import com.example.musicplayer.ui.viewModel.state.PlayListUiState

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
fun ScalingLazyListScope.playListUiStateContent(
    playListUiState: PlayListUiState,
    playListViewModel: PlayListViewModel,
    navController: NavController,
    trackId: String?
) {
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
            itemsIndexed(playListUiState.plaLists, key = { index, _ -> index }) { _, item ->
                PlayListChip(
                    text = item.title,
                    navController = navController,
                    trackId = trackId,
                    onSwipe = {
                        playListViewModel.deletePlaylist(item.title)
                    }
                )
            }
    }
}