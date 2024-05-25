package com.example.musicplayer.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.media3.common.util.Log
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.data.LocalDownloadRepository
import com.example.musicplayer.data.model.Track
import com.example.musicplayer.download.DownloadManagerImpl
import com.example.musicplayer.ui.viewModel.state.DownloadTrackState
import com.example.musicplayer.ui.viewModel.state.LoadTrackState
import com.example.musicplayer.ui.viewModel.state.TrackListState
import com.example.musicplayer.ui.viewModel.state.TrackUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

class DownloadViewModel(
    private val localDownloadRepository: LocalDownloadRepository
) : ViewModel() {

    var downloadTrackState: DownloadTrackState by mutableStateOf(DownloadTrackState.Empty)

    var loadState: LoadTrackState by mutableStateOf(LoadTrackState.Unload)

    val fileSize = MutableLiveData<Long>()

    fun getDownloadedTracks() {
        viewModelScope.launch {
            downloadTrackState = try {
                val tracks = localDownloadRepository.getDownloadedTracks()
                if (tracks.isEmpty()) {
                    DownloadTrackState.Empty
                } else {
                    DownloadTrackState.NotEmpty(trackList = tracks)
                }
            } catch (e: IOException) {
                DownloadTrackState.Empty
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
                    -1L
                }
            }
            val sizeInMB = if (sizeInBytes >= 0) sizeInBytes / (1024 * 1024) else sizeInBytes
            fileSize.value = sizeInMB
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MusicApplication)
                val localDownloadRepository = application.container.localDownloadRepository
                DownloadViewModel(localDownloadRepository = localDownloadRepository)
            }
        }
    }
}
