package com.example.musicplayer.ui.viewModel.state

import com.example.musicplayer.data.model.Track

interface DownloadTrackState {
        object Empty : DownloadTrackState
        data class NotEmpty(val trackList: List<Track>) : DownloadTrackState
}