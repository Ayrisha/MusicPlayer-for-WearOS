package com.example.musicplayer.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import com.example.musicplayer.DefaultAppContainer
import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.ui.viewModel.state.LikeState
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.media.data.repository.PlayerRepositoryImpl
import com.google.android.horologist.media.ui.state.PlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@UnstableApi
@OptIn(ExperimentalHorologistApi::class)
class PlayerViewModel(
    private val player: MediaController,
    private val playerRepository: PlayerRepositoryImpl = PlayerRepositoryImpl(),
    private val musicPlayerRepository: MusicPlayerRepository = DefaultAppContainer().musicPlayerRepository
) : PlayerViewModel(playerRepository) {

    var likeState: LikeState by mutableStateOf(LikeState.Dislike)

    init {
        viewModelScope.launch {
            playerRepository.connect(player) {}

            player.currentMediaItem?.mediaId?.let { checkTrack(it) }

            player.addListener(
                object: Player.Listener {
                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        super.onMediaItemTransition(mediaItem, reason)

                        val newMediaItem = mediaItem?.mediaId
                        newMediaItem?.let { checkTrack(it) }
                    }
                }
            )
        }
    }

    fun setLike(idMedia: String){
        viewModelScope.launch {
            musicPlayerRepository.setTrackLike(idMedia)
            checkTrack(idMedia)
        }
    }

    fun deleteLike(idMedia: String){
        viewModelScope.launch {
            musicPlayerRepository.deleteTrackLike(idMedia)
            checkTrack(idMedia)
        }
    }

    private suspend fun checkLikeResponse(idMedia: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                musicPlayerRepository.checkTrackLike(idMedia)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    fun checkTrack(idMedia: String) {
        viewModelScope.launch {
            val result = checkLikeResponse(idMedia)
            likeState = if (result) {
                LikeState.Like
            } else {
                LikeState.Dislike
            }
        }
    }
}

