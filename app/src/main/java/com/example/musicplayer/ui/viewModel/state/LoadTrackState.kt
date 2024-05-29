package com.example.musicplayer.ui.viewModel.state

sealed interface LoadTrackState {
    object Load : LoadTrackState
    object Unload : LoadTrackState
    data class Progress(val percent: Int) : LoadTrackState
}
