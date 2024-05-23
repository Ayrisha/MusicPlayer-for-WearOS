package com.example.musicplayer.ui.viewModel.state

import com.example.musicplayer.data.model.Track

sealed interface TrackListState{
    data class Success(val tracks: List<Track>) : TrackListState
    object Empty : TrackListState
    object NotRegister : TrackListState
    object Error : TrackListState
    object Loading : TrackListState
}