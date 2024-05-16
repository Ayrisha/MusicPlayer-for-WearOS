package com.example.musicplayer.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.ScalingLazyListAnchorType
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.scrollAway
import com.example.musicplayer.ui.components.SearchBar
import com.example.musicplayer.ui.viewModel.RecentSearchViewModel
import androidx.wear.compose.material.Text
import com.example.musicplayer.ui.viewModel.state.RecentSearchState

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

    Scaffold(
        positionIndicator = {
            PositionIndicator(listState)
        }
    ) {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
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
                            recentSearchViewModel.updateSearchTextState(it)
                                recentSearchViewModel.setSearch(it)
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                "search_query",
                                it
                            )
                            navController.popBackStack()
                        }
                    )
                }
                when (recentSearchUiState) {
                    is RecentSearchState.Empty -> item {
                        ListHeader {
                        Text(
                            text = "Нет последних поисковых запросов",
                            color = Color.White.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center
                        )
                    }
                    }

                    is RecentSearchState.Success -> {
                        item {
                            ListHeader {
                                Text(
                                    text = "Последние запросы",
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