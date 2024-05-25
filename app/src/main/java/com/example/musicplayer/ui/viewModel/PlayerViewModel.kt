package com.example.musicplayer.ui.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.download.DownloadManagerImpl
import com.example.musicplayer.ui.viewModel.state.LikeState
import com.example.musicplayer.ui.viewModel.state.LoadTrackState
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.media.data.repository.PlayerRepositoryImpl
import com.google.android.horologist.media.ui.state.PlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

@UnstableApi
@OptIn(ExperimentalHorologistApi::class)
class PlayerViewModel(
    private val player: MediaController,
    private val playerRepository: PlayerRepositoryImpl = PlayerRepositoryImpl(),
    context: Context
) : PlayerViewModel(playerRepository) {

    var likeState: LikeState by mutableStateOf(LikeState.Dislike)

    var loadState: LoadTrackState by mutableStateOf(LoadTrackState.Unload)

    val fileSize = MutableLiveData<Long>()

    private lateinit var musicPlayerRepository: MusicPlayerRepository

    init {
        viewModelScope.launch {
            playerRepository.connect(player) {}

            val appContainer = (context.applicationContext as MusicApplication).container
            musicPlayerRepository = appContainer.musicPlayerRepository
            val downloadManagerImpl = appContainer.downloadManagerImpl

            player.currentMediaItem?.mediaId?.let {
                checkLikeTrack(it)
                checkLoadTrack(it, downloadManagerImpl)
            }

            player.addListener(
                object: Player.Listener {
                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        super.onMediaItemTransition(mediaItem, reason)

                        val newMediaItem = mediaItem?.mediaId
                        newMediaItem?.let {
                            checkLikeTrack(it)
                            checkLoadTrack(it, downloadManagerImpl)
                        }
                    }
                }
            )
        }
    }

    fun setLike(idMedia: String){
        viewModelScope.launch {
            musicPlayerRepository.setTrackLike(idMedia)
            checkLikeTrack(idMedia)
        }
    }

    fun deleteLike(idMedia: String){
        viewModelScope.launch {
            musicPlayerRepository.deleteTrackLike(idMedia)
            checkLikeTrack(idMedia)
        }
    }

    fun getFileSizeOfUrl(url: String) {
        viewModelScope.launch {
            val sizeInBytes = withContext(Dispatchers.IO) {
                try {
                    val uri = URL(url)
                    val urlConnection = uri.openConnection()
                    urlConnection.connect()
                    val contentLengthStr = urlConnection.getHeaderField("content-length")
                    if (contentLengthStr.isNullOrEmpty()) {
                        -1L
                    } else {
                        contentLengthStr.toLong()
                    }
                } catch (e: Exception) {
                    Log.d("GetFileSizeOfUrl", "Error: ${e.message}")
                    -1L
                }
            }
            val sizeInMB = if (sizeInBytes >= 0) sizeInBytes / (1024 * 1024) else sizeInBytes
            fileSize.value = sizeInMB
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

    fun checkLikeTrack(idMedia: String) {
        viewModelScope.launch {
            val result = checkLikeResponse(idMedia)
            likeState = if (result) {
                LikeState.Like
            } else {
                LikeState.Dislike
            }
        }
    }

    fun checkLoadTrack(idMedia: String, downloadManagerImpl: DownloadManagerImpl) {
        viewModelScope.launch {
            val result = downloadManagerImpl.isSongDownloaded(idMedia)
            loadState = if (result) {
                LoadTrackState.Load
            } else {
                LoadTrackState.Unload
            }
        }
    }
}

