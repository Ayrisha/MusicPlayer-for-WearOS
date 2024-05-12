package com.example.musicplayer.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import com.example.musicplayer.ui.viewModel.TrackViewModel
import com.example.musicplayer.ui.components.EmptyBox
import com.example.musicplayer.ui.components.Retry
import com.example.musicplayer.ui.components.Loading
import com.example.musicplayer.ui.components.SearchBar
import com.example.musicplayer.ui.components.SongCard
import com.example.musicplayer.ui.viewModel.state.TrackUiState


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SearchScreen(
    mediaController: MediaController,
    navController: NavController
) {
    val trackViewModel: TrackViewModel = viewModel(factory = TrackViewModel.Factory)

    val songUiState = trackViewModel.trackUiState

    val searchTextState = trackViewModel.searchTextState

    val keyboardController = LocalSoftwareKeyboardController.current

    val listState = rememberScalingLazyListState()

    if (searchTextState.value.isEmpty()) {
        trackViewModel.popularTrack()
    }

    Scaffold(
        vignette = {
            Vignette(vignettePosition = VignettePosition.Bottom)
        },
        positionIndicator = {
            PositionIndicator(listState)
        },
        timeText = { TimeText(modifier = Modifier.scrollAway(listState)) }
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            item {
                ListHeader {
                    Text(text = "Поиск")
                }
            }
            item {
                SearchBar(
                    text = searchTextState.value,
                    onTextChange = {
                        trackViewModel.updateSearchTextState(it)
                    },
                    onSearchClicked = {
                        trackViewModel.searchTrack(it)
                    },
                    keyboardController = keyboardController
                )
            }
            when (songUiState) {
                is TrackUiState.Start ->
                    itemsIndexed(songUiState.trackPopular) { index, item ->
                        SongCard(
                            index = index,
                            mediaController = mediaController,
                            list = songUiState.trackPopular,
                            navController = navController,
                            id = item.id,
                            title = item.title,
                            artist = item.artist,
                            img = item.imgLink
                        )
                    }

                is TrackUiState.Success ->
                    itemsIndexed(songUiState.trackSearches) { index, item ->
                        SongCard(
                            index = index,
                            mediaController = mediaController,
                            list = songUiState.trackSearches,
                            navController = navController,
                            id = item.id,
                            title = item.title,
                            artist = item.artist,
                            img = item.imgLink
                        )
                    }

                is TrackUiState.Empty -> item {
                    EmptyBox("По вашему запросу ничего не найдено")
                }

                is TrackUiState.Loading -> item { Loading() }
                is TrackUiState.Error ->
                    item {
                        Retry(
                            retryAction = {
                                trackViewModel.searchTrack(
                                    searchTextState.value
                                )
                            })
                    }
            }
        }
    }
}
