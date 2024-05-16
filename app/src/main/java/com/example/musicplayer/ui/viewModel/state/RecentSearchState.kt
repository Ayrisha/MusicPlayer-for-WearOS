package com.example.musicplayer.ui.viewModel.state

sealed interface RecentSearchState {
    data class Success(val trackSearches: List<String>) : RecentSearchState
    object Empty : RecentSearchState
}