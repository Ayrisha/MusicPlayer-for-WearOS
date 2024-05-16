package com.example.musicplayer.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.session.MediaController
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
import androidx.wear.compose.material.scrollAway
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.viewModel.SearchViewModel
import com.example.musicplayer.ui.components.EmptyBox
import com.example.musicplayer.ui.components.Retry
import com.example.musicplayer.ui.components.Loading
import com.example.musicplayer.ui.components.SongCard
import com.example.musicplayer.ui.viewModel.state.TrackUiState


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SearchScreen(
    mediaController: MediaController,
    navController: NavController
) {
    val trackViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)

    val songUiState = trackViewModel.trackUiState

    val mediaManager = MediaManager(mediaController)

    val listState = rememberScalingLazyListState()

    val stateHeader = mutableStateOf("Популярные треки")

    val searchQuery =
        navController.currentBackStackEntry?.savedStateHandle?.get<String>("search_query")

    val searchQueryState = remember {
        mutableStateOf(searchQuery)
    }

    if (searchQuery != null) {
        stateHeader.value = searchQuery
        trackViewModel.searchTrack(searchQuery)
    }else{
        trackViewModel.popularTrack()
    }

    Scaffold(
        positionIndicator = {
            PositionIndicator(listState)
        },
        timeText = { TimeText(modifier = Modifier.scrollAway(listState)) }
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
                        navController.navigate("searchscreen")
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(48, 79, 254)
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "item image",
                            tint = Color.White
                        )
                    }
                }
            }
            item {
                ListHeader {
                    Text(
                        text = stateHeader.value,
                        textAlign = TextAlign.Center)
                }
            }
            when (songUiState) {
                is TrackUiState.Start -> {
                    stateHeader.value = "Популярные треки"
                    itemsIndexed(
                        songUiState.trackPopular
                    ) { index, item ->
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

                is TrackUiState.Loading -> item { Loading() }
                is TrackUiState.Error -> {
                    item {
                        Retry(
                            retryAction = {
                                if (searchQuery != null) {
                                    searchQueryState.value?.let {
                                        trackViewModel.searchTrack(
                                            it
                                        )
                                    }
                                } else {
                                    trackViewModel.popularTrack()
                                }
                            })
                    }
                }
            }
        }
    }
}
