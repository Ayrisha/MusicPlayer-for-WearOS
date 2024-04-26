package com.example.musicplayer.ui.viewModel

import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import com.example.musicplayer.data.Track
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.media.data.repository.PlayerRepositoryImpl
import com.google.android.horologist.media.model.Media
import com.google.android.horologist.media.ui.state.PlayerViewModel
import kotlinx.coroutines.launch

@UnstableApi
@OptIn(ExperimentalHorologistApi::class)
class PlayerViewModel(
    private val player: MediaController,
    private val playerRepository: PlayerRepositoryImpl = PlayerRepositoryImpl()
) : PlayerViewModel(playerRepository) {
    init {
        viewModelScope.launch {
            playerRepository.connect(player) {}
        }
    }

    fun setTrack(id: String, title: String, artist: String) {
        viewModelScope.launch {
            if (playerRepository.currentMedia.value == null || playerRepository.currentMedia.value!!.id != id) {
                playerRepository.setMedia(
                    Media(
                        id = id,
                        uri = "http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/music/$id.mp3",
                        title = title,
                        artist = artist
                    )
                )
            }
        }
    }

    fun setTrackList(idList: List<String>, index: Int) {
        val mediaList = idList.map { id ->
            Media(
                id = id,
                title = "Title",
                artist = "Artist",
                uri = "http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/music/$id.mp3"
            )
        }
        viewModelScope.launch {
            playerRepository.setMediaList(mediaList, index)
        }
    }
}

