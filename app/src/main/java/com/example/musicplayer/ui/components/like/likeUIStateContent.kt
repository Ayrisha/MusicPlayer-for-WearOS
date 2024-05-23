package com.example.musicplayer.ui.components.like

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyListScope
import androidx.wear.compose.foundation.lazy.itemsIndexed
import com.example.musicplayer.ui.components.EmptyBox
import com.example.musicplayer.ui.components.Loading
import com.example.musicplayer.ui.components.NotRegister
import com.example.musicplayer.ui.components.Retry
import com.example.musicplayer.ui.components.SwipeSongCard
import com.example.musicplayer.ui.viewModel.LikeViewModel
import com.example.musicplayer.ui.viewModel.state.TrackListState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
fun ScalingLazyListScope.likeUiStateContent(
    songUiState: TrackListState,
    navController: NavController,
    likeViewModel: LikeViewModel,
    mediaController: MediaController,
    onClick: () -> Unit
) {
    when (songUiState) {
        is TrackListState.Success -> {
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
                        item.id?.let { likeViewModel.deleteTrackLike(item.id) }
                    },
                    onClick = {
                        onClick()
                    }
                )
            }
        }

        is TrackListState.NotRegister -> item{
            NotRegister(
                registerAction = {
                    navController.navigate("auth")
                }
            )
        }

        is TrackListState.Empty -> item {
            EmptyBox("Нет избранных")
        }

        is TrackListState.Loading -> item {
            Loading()
        }

        is TrackListState.Error -> item {
            Retry(
                retryAction = {
                    likeViewModel.getTracksLike()
                })
        }
    }
}