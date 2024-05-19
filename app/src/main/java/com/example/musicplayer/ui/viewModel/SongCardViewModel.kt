package com.example.musicplayer.ui.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.musicplayer.data.model.Track

class SongCardViewModel: ViewModel() {
    private val _tracks = mutableStateListOf<Track>()
    val tracks: SnapshotStateList<Track> get() = _tracks

    fun removeTrack(track: Track) {
        _tracks.remove(track)
    }

    fun loadTracks(newTracks: List<Track>) {
        _tracks.clear()
        _tracks.addAll(newTracks)
    }
}