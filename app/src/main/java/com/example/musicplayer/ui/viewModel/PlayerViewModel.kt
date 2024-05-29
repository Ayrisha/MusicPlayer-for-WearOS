package com.example.musicplayer.ui.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.ui.viewModel.state.LikeState
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.media.data.repository.PlayerRepositoryImpl
import com.google.android.horologist.media.ui.state.PlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException

@UnstableApi
@OptIn(ExperimentalHorologistApi::class)
class PlayerViewModel(
    private val player: MediaController,
    private val playerRepository: PlayerRepositoryImpl = PlayerRepositoryImpl(),
    context: Context
) : PlayerViewModel(playerRepository) {

    var likeState: LikeState by mutableStateOf(LikeState.Dislike)

    private lateinit var musicPlayerRepository: MusicPlayerRepository

    init {
        viewModelScope.launch {
            playerRepository.connect(player) {}

            val appContainer = (context.applicationContext as MusicApplication).container
            musicPlayerRepository = appContainer.musicPlayerRepository

            player.currentMediaItem?.mediaId?.let {
                checkLikeTrack(it)
            }

            player.addListener(
                object : Player.Listener {
                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        super.onMediaItemTransition(mediaItem, reason)

                        val newMediaItem = mediaItem?.mediaId
                        newMediaItem?.let {
                            checkLikeTrack(it)
                        }
                    }
                }
            )
        }
    }

    fun setLike(idMedia: String) {
        viewModelScope.launch {
            try {
                musicPlayerRepository.setTrackLike(idMedia)
                checkLikeTrack(idMedia)
            } catch (_: Exception) {

            }
        }
    }

    fun deleteLike(idMedia: String) {
        viewModelScope.launch {
            try {
                musicPlayerRepository.deleteTrackLike(idMedia)
                checkLikeTrack(idMedia)
            } catch (_: Exception) {

            }
        }
    }

    private suspend fun checkLikeResponse(idMedia: String): Int {
        return withContext(Dispatchers.IO) {
            try {
                musicPlayerRepository.checkTrackLike(idMedia)
                1
            } catch (e: ConnectException) {
                LikeState.NotConnection
                2
            } catch (e: Exception) {
                0
            }
        }
    }

    fun checkLikeTrack(idMedia: String) {
        viewModelScope.launch {
            val result = checkLikeResponse(idMedia)
            likeState = if (result == 1) {
                LikeState.Like
            } else if (result == 0){
                LikeState.Dislike
            } else{
                LikeState.NotConnection
            }
        }
    }
}

