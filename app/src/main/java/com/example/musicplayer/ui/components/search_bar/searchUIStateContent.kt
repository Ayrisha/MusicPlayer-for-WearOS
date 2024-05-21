package com.example.musicplayer.ui.components.search_bar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyListScope
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.Text
import com.example.musicplayer.ui.viewModel.RecentSearchViewModel
import com.example.musicplayer.ui.viewModel.state.RecentSearchState

fun ScalingLazyListScope.searchUiStateContent(
    recentSearchUiState: RecentSearchState,
    navController: NavController,
    recentSearchViewModel: RecentSearchViewModel,
) {
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