package com.example.musicplayer.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.scrollAway
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.viewModel.SearchViewModel
import com.example.musicplayer.ui.components.search.SearchButton
import com.example.musicplayer.ui.components.search.SearchHeader
import com.example.musicplayer.ui.components.search.trackUiStateContent
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.rotaryinput.ScalingLazyColumnRotaryScrollAdapter
import com.google.android.horologist.compose.rotaryinput.rotaryWithSnap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalHorologistApi::class, ExperimentalWearFoundationApi::class,
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
    val searchQuery = navController.currentBackStackEntry?.savedStateHandle?.get<String>("search_query")
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
            item { SearchButton(navController) }
            item { SearchHeader(stateHeader.value) }
            trackUiStateContent(
                songUiState = songUiState,
                mediaManager = mediaManager,
                trackViewModel = trackViewModel,
                navController = navController,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                }
            )
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
