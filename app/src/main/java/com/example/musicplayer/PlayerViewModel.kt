package com.example.musicplayer

import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.media.data.repository.PlayerRepositoryImpl
import com.google.android.horologist.media.model.Media
import com.google.android.horologist.media.ui.state.PlayerViewModel
import kotlinx.coroutines.launch

@UnstableApi @OptIn(ExperimentalHorologistApi::class)
class PlayerViewModel(
    private val player: ExoPlayer,
    private val playerRepository: PlayerRepositoryImpl = PlayerRepositoryImpl()
) : PlayerViewModel(playerRepository) {
    init {
        viewModelScope.launch {
            playerRepository.connect(player) {}
        }
    }
    fun setTrack(id: String){
        viewModelScope.launch {
            playerRepository.setMedia(Media(id,"http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/music/$id.mp3", "Test", "Test"))
        }
    }
}

