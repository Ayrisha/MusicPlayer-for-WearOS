package com.example.musicplayer.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.example.musicplayer.TrackUiState
import com.example.musicplayer.TrackViewModel
import com.example.musicplayer.ui.components.EmptyBox
import com.example.musicplayer.ui.components.Retry
import com.example.musicplayer.ui.components.Loading
import com.example.musicplayer.ui.components.SearchBar
import com.example.musicplayer.ui.components.SongList


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SearchScreen(
    navController: NavController
) {
    val trackViewModel: TrackViewModel = viewModel(factory = TrackViewModel.Factory)

    val songUiState = trackViewModel.trackUiState

    val searchTextState = trackViewModel.searchTextState

    val keyboardController = LocalSoftwareKeyboardController.current

    val listState = rememberLazyListState()

    trackViewModel.popularTrack()

    Scaffold(
        timeText = {
            TimeText()
        },
        vignette = {
            Vignette(vignettePosition = VignettePosition.Bottom)
        },
        positionIndicator = {
            PositionIndicator(lazyListState = listState)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            when (songUiState) {
                is TrackUiState.Success ->
                    SongList(
                        listState = listState,
                        listTrack = songUiState.trackSearches,
                        navController = navController)
                is TrackUiState.Start ->
                    SongList(
                        listState = listState,
                        listTrack = songUiState.trackPopular,
                        navController = navController)
                is TrackUiState.Empty -> EmptyBox()
                is TrackUiState.Loading -> Loading()
                is TrackUiState.Error ->
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
