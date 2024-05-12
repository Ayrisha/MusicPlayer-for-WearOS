package com.example.musicplayer.ui.viewModel.state

import com.example.musicplayer.data.model.Track

sealed interface TrackUiState{
    data class Success(val trackSearches: List<Track>) : TrackUiState
    data class Start(val trackPopular: List<Track>) : TrackUiState
    object Empty : TrackUiState
    object Error : TrackUiState
    object Loading : TrackUiState
}