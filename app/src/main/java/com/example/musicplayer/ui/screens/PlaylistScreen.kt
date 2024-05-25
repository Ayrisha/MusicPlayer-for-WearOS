package com.example.musicplayer.ui.screens

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.scrollAway
import com.example.musicplayer.ui.components.playlist.AddPlaylistButton
import com.example.musicplayer.ui.components.playlist.PlaylistHeader
import com.example.musicplayer.ui.components.playlist.playListUiStateContent
import com.example.musicplayer.ui.viewModel.PlayListViewModel
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.rotaryinput.ScalingLazyColumnRotaryScrollAdapter
import com.google.android.horologist.compose.rotaryinput.rotaryWithSnap
import kotlinx.coroutines.launch

@OptIn(ExperimentalHorologistApi::class, ExperimentalWearFoundationApi::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PlaylistScreen(
    navController: NavController,
    trackId: String? = null
) {
    val context = LocalContext.current

    val playListViewModel: PlayListViewModel = viewModel(factory = PlayListViewModel.Factory)

    val listState = rememberScalingLazyListState()

    val playListUiState = playListViewModel.playListUiState

    val coroutineScope = rememberCoroutineScope()

    val rotaryScrollAdapter = remember { ScalingLazyColumnRotaryScrollAdapter(listState) }

    val focusRequester = rememberActiveFocusRequester()

    playListViewModel.getPlaylists()

    Scaffold(
        positionIndicator = {
            PositionIndicator(listState)
        },
        timeText = { TimeText(
            modifier = Modifier.scrollAway(listState)) }
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
                AddPlaylistButton(navController = navController)
            }
            item {
                PlaylistHeader()
            }
            playListUiStateContent(
                playListUiState = playListUiState,
                playListViewModel = playListViewModel,
                navController = navController,
                trackId = trackId
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            val vibrator: Vibrator = context.getSystemService(Vibrator::class.java)
            val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            vibrator.vibrate(effect)
        }
    }
}
