package com.example.musicplayer.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListAnchorType
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import com.example.musicplayer.ui.components.search_bar.SearchBar
import com.example.musicplayer.ui.viewModel.RecentSearchViewModel
import com.example.musicplayer.ui.components.search_bar.searchUiStateContent
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.rotaryinput.ScalingLazyColumnRotaryScrollAdapter
import com.google.android.horologist.compose.rotaryinput.rotaryWithSnap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalWearFoundationApi::class, ExperimentalHorologistApi::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SearchBarScreen(
    navController: NavController
) {
    val recentSearchViewModel: RecentSearchViewModel =
        viewModel(factory = RecentSearchViewModel.Factory)
    val recentSearchUiState = recentSearchViewModel.recentSearchState
    val searchTextState by recentSearchViewModel.getTextState
    val listState = rememberScalingLazyListState()
    val focusRequester = rememberActiveFocusRequester()
    val coroutineScope = rememberCoroutineScope()
    val rotaryScrollAdapter = ScalingLazyColumnRotaryScrollAdapter(listState)

    Scaffold(
        positionIndicator = {
            PositionIndicator(listState)
        }
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
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
            anchorType = ScalingLazyListAnchorType.ItemStart
        ) {
            item {
                SearchBar(
                    text = searchTextState,
                    onTextChange = {
                        recentSearchViewModel.updateSearchTextState(it)
                    },
                    onSearchClicked = {
                        coroutineScope.launch {
                            recentSearchViewModel.updateSearchTextState(it)
                            recentSearchViewModel.setSearch(it)
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                "search_query",
                                it
                            )
                            navController.popBackStack()
                        }
                    }
                )
            }
            item { Spacer(Modifier.height(10.dp)) }
            searchUiStateContent(
                recentSearchUiState = recentSearchUiState,
                navController = navController,
                recentSearchViewModel = recentSearchViewModel
            )
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            navController.previousBackStackEntry?.savedStateHandle?.set(
                "search_query",
                null
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}