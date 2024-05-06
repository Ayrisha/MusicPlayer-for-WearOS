package com.example.musicplayer.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.example.musicplayer.ui.components.EmptyBox
import com.example.musicplayer.ui.components.Loading
import com.example.musicplayer.ui.components.Retry
import com.example.musicplayer.ui.components.SongCard
import com.example.musicplayer.ui.viewModel.LikeUiState
import com.example.musicplayer.ui.viewModel.LikeViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun LikeScreen(
    mediaController: MediaController,
    navController: NavController
) {
    val likeViewModel: LikeViewModel = viewModel(factory = LikeViewModel.Factory)

    val songUiState = likeViewModel.likeUiState

    val listState = rememberLazyListState()

    likeViewModel.getTracksLike()

    Scaffold(
        vignette = {
            Vignette(vignettePosition = VignettePosition.Bottom)
        },
        positionIndicator = {
            PositionIndicator(lazyListState = listState)
        }
    ) {
        when (songUiState) {
            is LikeUiState.Success ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = listState,
                    contentPadding = PaddingValues(
                        top = 30.dp,
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 10.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    itemsIndexed(songUiState.tracksLike) { index, item ->
                        SongCard(
                            index = index,
                            mediaController = mediaController,
                            list = songUiState.tracksLike,
                            navController = navController,
                            id = item.id,
                            title = item.title,
                            artist = item.artist,
                            img = item.imgLink
                        )
                    }
                }

            is LikeUiState.Empty -> {
                EmptyBox("Нет избранных")
            }

            is LikeUiState.Loading -> {
                Loading()
            }

            is LikeUiState.Error -> {
                Retry(
                    retryAction = {
                        likeViewModel.getTracksLike()
                    })
            }
        }
    }
}

