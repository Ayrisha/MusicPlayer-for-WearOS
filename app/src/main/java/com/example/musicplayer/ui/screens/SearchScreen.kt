package com.example.musicplayer.ui.screens

import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.dialog.Dialog
import androidx.wear.compose.material.scrollAway
import com.example.musicplayer.data.model.Track
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.components.EmptyBox
import com.example.musicplayer.ui.components.Loading
import com.example.musicplayer.ui.components.NotRegister
import com.example.musicplayer.ui.components.Retry
import com.example.musicplayer.ui.components.SongCard
import com.example.musicplayer.ui.viewModel.SearchViewModel
import com.example.musicplayer.ui.components.search.SearchButton
import com.example.musicplayer.ui.components.search.SearchHeader
import com.example.musicplayer.ui.viewModel.state.TrackUiState
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.rotaryinput.ScalingLazyColumnRotaryScrollAdapter
import com.google.android.horologist.compose.rotaryinput.rotaryWithSnap
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@UnstableApi @OptIn(
    ExperimentalHorologistApi::class, ExperimentalWearFoundationApi::class,
    ExperimentalFoundationApi::class
)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SearchScreen(
    mediaController: MediaController,
    navController: NavController,
    pagerState: PagerState
) {
    val context = LocalContext.current

    val trackViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)

    val songUiState = trackViewModel.trackUiState

    val mediaManager = MediaManager(mediaController)

    val listState = rememberScalingLazyListState()

    val searchQuery =
        navController.currentBackStackEntry?.savedStateHandle?.get<String>("search_query")

    val stateHeader = remember { mutableStateOf("Популярные треки") }

    val focusRequester = rememberActiveFocusRequester()

    val coroutineScope = rememberCoroutineScope()

    val rotaryScrollAdapter = remember { ScalingLazyColumnRotaryScrollAdapter(listState) }


    LaunchedEffect(searchQuery) {
        if (searchQuery != null) {
            trackViewModel.searchTrack(searchQuery)
            stateHeader.value = searchQuery
        } else {
            trackViewModel.popularTrack()
            stateHeader.value = "Популярные треки"
        }
    }

    Scaffold(
        positionIndicator = { PositionIndicator(listState) },
        timeText = { TimeText(modifier = Modifier.scrollAway(listState)) }
    ) {
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .onRotaryScrollEvent {
                    coroutineScope.launch {
                        listState.scrollBy(it.verticalScrollPixels)
                    }
                    true
                }
                .rotaryWithSnap(rotaryScrollAdapter = rotaryScrollAdapter)
                .focusRequester(focusRequester)
                .focusable(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { SearchButton(navController, songUiState, mediaManager, pagerState) }
            item { SearchHeader(stateHeader.value) }
            when (songUiState) {
                is TrackUiState.Start -> {
                    itemsIndexed(songUiState.trackPopular) { index, item ->
                        SongCard(
                            id = item.id,
                            title = item.title,
                            artist = item.artist,
                            img = "http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/image/${item.imgLink}.png",
                            onClick = {
                                val image = Uri.encode("http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/image/${item.imgLink}.png")
                                navController.navigate(Routes.SongInfoScreen + "/${item.title}/${item.artist}/${item.id}/${image}")
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
                                val image = Uri.encode("http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/image/${item.imgLink}.png")
                                navController.navigate(Routes.SongInfoScreen + "/${item.title}/${item.artist}/${item.id}/${image}")
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
    }

    DisposableEffect(Unit) {
        onDispose {
            val vibrator = context.getSystemService(Vibrator::class.java)
            vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
