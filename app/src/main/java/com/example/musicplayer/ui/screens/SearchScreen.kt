package com.example.musicplayer.ui.screens

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
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
import com.example.musicplayer.ui.viewModel.SongCardViewModel
import com.example.musicplayer.ui.viewModel.state.TrackUiState
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.rotaryinput.ScalingLazyColumnRotaryScrollAdapter
import com.google.android.horologist.compose.rotaryinput.rotaryWithSnap
import kotlinx.coroutines.launch


@OptIn(ExperimentalHorologistApi::class, ExperimentalWearFoundationApi::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SearchScreen(
    mediaController: MediaController,
    navController: NavController
) {
    val context = LocalContext.current

    val trackViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)

    val songCardViewModel: SongCardViewModel = viewModel()

    val tracks = songCardViewModel.tracks

    val songUiState = trackViewModel.trackUiState

    val mediaManager = MediaManager(mediaController)

    val listState = rememberScalingLazyListState()

    val searchQuery =
        navController.currentBackStackEntry?.savedStateHandle?.get<String>("search_query")

    val stateHeader = remember {
        mutableStateOf("Популярные треки")}

    val focusRequester = rememberActiveFocusRequester()

    val coroutineScope = rememberCoroutineScope()

    val rotaryScrollAdapter = ScalingLazyColumnRotaryScrollAdapter(listState)

    DisposableEffect(Unit) {
        onDispose {
            val vibrator: Vibrator = context.getSystemService(Vibrator::class.java)
            val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            vibrator.vibrate(effect)
        }
    }

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
        positionIndicator = {
            PositionIndicator(listState)
        },
        timeText = { TimeText(modifier = Modifier.scrollAway(listState)) }
    ) {

        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .onRotaryScrollEvent {
                    coroutineScope.launch {
                        listState.scrollBy(it.verticalScrollPixels)
                        listState.animateScrollBy(0f)
                    }
                    true
                }
                .rotaryWithSnap(
                    rotaryScrollAdapter = rotaryScrollAdapter
                )
                .focusRequester(focusRequester)
                .focusable(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
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
                        textAlign = TextAlign.Center
                    )
                }
            }
            when (songUiState) {
                is TrackUiState.Start -> {
                    songCardViewModel.loadTracks(songUiState.trackPopular)
                    itemsIndexed(
                        tracks
                    ) { index, item ->
                        SongCard(
                            title = item.title,
                            artist = item.artist,
                            img = item.imgLink,
                            onClick = {
                                mediaManager.setMediaItems(tracks, index)
                                navController.navigate("play_screen")
                            }
                        )
                    }
                }

                is TrackUiState.Success -> {
                    songCardViewModel.loadTracks(songUiState.trackSearches)
                    itemsIndexed(tracks) { index, item ->
                        SongCard(
                            title = item.title,
                            artist = item.artist,
                            img = item.imgLink,
                            onClick = {
                                mediaManager.setMediaItems(tracks, index)
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
                                        trackViewModel.searchTrack(
                                            searchQuery)
                                } else {
                                    trackViewModel.popularTrack()
                                }
                            })
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
