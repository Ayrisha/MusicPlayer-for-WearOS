package com.example.musicplayer

import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.media.data.repository.PlayerRepositoryImpl
import com.google.android.horologist.media.model.Media
import com.google.android.horologist.media.ui.state.PlayerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalHorologistApi::class)
class MyViewModel(
    player: ExoPlayer,
    playerRepository: PlayerRepositoryImpl = PlayerRepositoryImpl()
) : PlayerViewModel(playerRepository) {
    init {
        viewModelScope.launch {
            playerRepository.connect(player) {}

            playerRepository.setMedia(
                Media(
                    id = "wake_up_02",
                    uri =  "https://storage.googleapis.com/uamp/The_Kyoto_Connection_-_Wake_Up/02_-_Geisha.mp3",
                    title = "Geisha",
                    artist = "The Kyoto Connection"
                )
            )
        }
    }
}
