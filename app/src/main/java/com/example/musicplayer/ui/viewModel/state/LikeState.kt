package com.example.musicplayer.ui.viewModel.state

sealed interface LikeState{
    object Like : LikeState
    object Dislike : LikeState
}