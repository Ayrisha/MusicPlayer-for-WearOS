package com.example.musicplayer.ui.screens

import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.scrollAway
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.components.EmptyBox
import com.example.musicplayer.ui.components.Loading
import com.example.musicplayer.ui.components.NotRegister
import com.example.musicplayer.ui.components.Retry
import com.example.musicplayer.ui.components.SongCard
import com.example.musicplayer.ui.components.playlist.PlayButtonPlaylist
import com.example.musicplayer.ui.viewModel.PlaylistTracksViewModel
import com.example.musicplayer.ui.viewModel.state.TrackListState
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.rotaryinput.ScalingLazyColumnRotaryScrollAdapter
import com.google.android.horologist.compose.rotaryinput.rotaryWithSnap
import kotlinx.coroutines.launch

@OptIn(ExperimentalHorologistApi::class, ExperimentalWearFoundationApi::class)
@ExperimentalFoundationApi
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PlayListTracksScreen(
    playlistName: String,
    mediaController: MediaController,
    navController: NavController,
    pagerState: PagerState
) {
    val context = LocalContext.current

    val tracksViewModel: PlaylistTracksViewModel =
        viewModel(factory = PlaylistTracksViewModel.Factory)

    val songUiState = tracksViewModel.likeUiState

    val listState = rememberScalingLazyListState()

    val coroutineScope = rememberCoroutineScope()

    val rotaryScrollAdapter = ScalingLazyColumnRotaryScrollAdapter(listState)

    val focusRequester = rememberActiveFocusRequester()

    tracksViewModel.getTracks(playlistName)

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
                .rotaryWithSnap(rotaryScrollAdapter = rotaryScrollAdapter)
                .focusRequester(focusRequester)
                .focusable(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                PlayButtonPlaylist(
                    navController,
                    songUiState,
                    MediaManager(mediaController),
                    pagerState,
                    playlistName
                )
            }

            item {
                ListHeader {
                    Text(text = playlistName)
                }
            }

            when (songUiState) {
                is TrackListState.Empty -> item {
                    EmptyBox("Нет добавленных треков")
                }

                is TrackListState.Loading -> item {
                    Loading()
                }

                is TrackListState.Error -> item {
                    Retry(
                        retryAction = {
                            tracksViewModel.getTracks(playlistName)
                        })
                }

                is TrackListState.NotRegister -> item {
                    NotRegister(
                        registerAction = {
                            navController.navigate(NavigationDestinations.Auth)
                        }
                    )
                }

                is TrackListState.Success -> {
                    itemsIndexed(songUiState.tracks) { index, item ->
                        SongCard(
                            id = item.id,
                            title = item.title,
                            artist = item.artist,
                            img = "http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/image/${item.imgLink}.png",
                            onClick = {
                                val image = Uri.encode("http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/image/${item.imgLink}.png")
                                navController.navigate(Routes.SongInfoScreen + "${item.title}/${item.artist}/${item.id}/${image}?playlist=${playlistName}")
                                      },
                            mediaController = mediaController
                        )
                    }
                }
            }

        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }


    DisposableEffect(Unit) {
        onDispose {
            val vibrator: Vibrator = context.getSystemService(Vibrator::class.java)
            val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            vibrator.vibrate(effect)
        }
    }
}