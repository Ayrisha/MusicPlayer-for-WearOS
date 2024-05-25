package com.example.musicplayer.ui.components.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.screens.Routes
import com.example.musicplayer.ui.viewModel.state.TrackUiState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchButton(
    navController: NavController,
    songUiState: TrackUiState,
    mediaManager: MediaManager,
    pagerState: PagerState
) {
    val coroutineScope  = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.size(ButtonDefaults.SmallButtonSize),
            onClick = {
                      when(songUiState){
                          TrackUiState.Empty -> {}
                          TrackUiState.Error -> {}
                          TrackUiState.Loading -> {}
                          TrackUiState.NotRegister -> {}
                          is TrackUiState.Start -> {
                              coroutineScope.launch {
                                  mediaManager.setMediaItems(songUiState.trackPopular, 0)
                                  pagerState.animateScrollToPage(1)
                              }
                          }
                          is TrackUiState.Success -> {
                              coroutineScope.launch {
                                  mediaManager.setMediaItems(songUiState.trackSearches, 0)
                                  pagerState.animateScrollToPage(1)
                              }
                          }
                      }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(48, 79, 254))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "item image",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.padding(5.dp))

        Button(
            modifier = Modifier.size(ButtonDefaults.SmallButtonSize),
            onClick = { navController.navigate(Routes.SearchBarScreen) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(48, 79, 254))
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
}