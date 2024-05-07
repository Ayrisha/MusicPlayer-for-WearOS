package com.example.musicplayer.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.example.musicplayer.ui.components.EmptyBox
import com.example.musicplayer.ui.components.Loading
import com.example.musicplayer.ui.components.PlayListChip
import com.example.musicplayer.ui.components.Retry
import com.example.musicplayer.ui.components.SearchBar
import com.example.musicplayer.ui.components.SongCard
import com.example.musicplayer.ui.viewModel.PlayListUiState
import com.example.musicplayer.ui.viewModel.PlayListViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PlaylistScreen(

) {
    val playListViewModel: PlayListViewModel = viewModel(factory = PlayListViewModel.Factory)

    val listState = rememberLazyListState()

    val playListUiState = playListViewModel.playListUiState

    val searchTextState = playListViewModel.getTextState

    val keyboardController = LocalSoftwareKeyboardController.current

    playListViewModel.getPlaylists()

    Scaffold(
        vignette = {
            Vignette(vignettePosition = VignettePosition.Bottom)
        },
        positionIndicator = {
            PositionIndicator(lazyListState = listState)
        }
    ) {
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
            item {
                SearchBar(
                    text = searchTextState.value,
                    onTextChange = {
                        playListViewModel.updateSearchTextState(it)
                    },
                    onSearchClicked = {
                        playListViewModel.setPlaylists(it)
                    },
                    keyboardController = keyboardController
                )
            }
            when (playListUiState) {
                is PlayListUiState.Empty -> item { EmptyBox(text = "Нет плейлистов") }
                is PlayListUiState.Error -> item {
                    Retry {

                    }
                }
                is PlayListUiState.Loading -> item{
                    Loading()
                }
                is PlayListUiState.Success ->
                    items(playListUiState.plaLists) { item ->
                        PlayListChip(text = item.title)
                }
            }
        }
    }
}