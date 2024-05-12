package com.example.musicplayer.ui.viewModel.state

import com.example.musicplayer.data.model.PlayList

sealed interface PlayListUiState{
    data class Success(val plaLists: List<PlayList>) : PlayListUiState
    object Empty : PlayListUiState
    object Error : PlayListUiState
    object Loading : PlayListUiState
}
