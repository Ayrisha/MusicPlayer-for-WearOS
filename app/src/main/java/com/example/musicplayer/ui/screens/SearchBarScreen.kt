package com.example.musicplayer.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListAnchorType
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import com.example.musicplayer.ui.components.SearchBar
import com.example.musicplayer.ui.viewModel.RecentSearchViewModel
import androidx.wear.compose.material.Text
import com.example.musicplayer.ui.viewModel.state.RecentSearchState
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.rotaryinput.ScalingLazyColumnRotaryScrollAdapter
import com.google.android.horologist.compose.rotaryinput.rotaryWithSnap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    DisposableEffect(Unit) {
        onDispose {
            navController.previousBackStackEntry?.savedStateHandle?.set(
                "search_query",
                null
            )
        }
    }

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
                                delay(100)
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "search_query",
                                    it
                                )
                                navController.popBackStack()
                            }
                        }
                    )
                }
                item{Spacer(Modifier.height(10.dp))}
                when (recentSearchUiState) {
                    is RecentSearchState.Empty -> item {
                        Text(
                            text = "Нет последних поисковых запросов",
                            color = Color.White.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center
                        )
                    }

                    is RecentSearchState.Success -> {
                        item {
                            ListHeader {
                                Text(
                                    text = "Последние поиск. запросы",
                                    color = Color.White.copy(alpha = 0.5f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        items(recentSearchUiState.trackSearches) { it ->

                            RecentSearchItem(
                                query = it,
                                onDeleteClicked = {
                                    recentSearchViewModel.removeRecentSearch(it)
                                },
                                onItemClicked = {
                                    recentSearchViewModel.updateSearchTextState(it)
                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                        "search_query",
                                        it
                                    )
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun RecentSearchItem(
    query: String,
    onDeleteClicked: () -> Unit,
    onItemClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked() }
            .background(
                color = Color(0xFF1C1B1F),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = query,
                style = TextStyle(color = Color.White)
            )
            IconButton(onClick = onDeleteClicked) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete",
                    tint = Color.Gray
                )
            }
        }
    }
}