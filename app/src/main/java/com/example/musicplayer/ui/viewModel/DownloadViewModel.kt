package com.example.musicplayer.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.download.DownloadManagerImpl
import com.example.musicplayer.ui.viewModel.state.DownloadTrackState
import com.example.musicplayer.ui.viewModel.state.LoadTrackState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

class DownloadViewModel(
    private val musicPlayerRepository: MusicPlayerRepository,
    private val downloadManagerImpl: DownloadManagerImpl
) : ViewModel() {

    var downloadTrackState: DownloadTrackState by mutableStateOf(DownloadTrackState.Empty)

    var loadState: LoadTrackState by mutableStateOf(LoadTrackState.Unload)

    val fileSize = MutableLiveData<Long>()

    init {
       downloadManagerImpl.downloadCompleteListener = { id ->
            checkLoadTrack(id, downloadManagerImpl)
        }
    }

    fun getDownloadedTracks() {
        viewModelScope.launch {
            downloadTrackState = try {
                val tracks = musicPlayerRepository.getDownloadedTracks()
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
            loadState = when (result) {
                LoadTrackState.Load -> {
                    Log.d("checkLoadTrack", "LoadTrackState.Load")
                    LoadTrackState.Load
                }
                is LoadTrackState.Progress -> {
                    Log.d("checkLoadTrack", "LoadTrackState.Progress: ${result.percent}")
                    LoadTrackState.Progress(result.percent)
                }
                LoadTrackState.Unload -> {
                    Log.d("checkLoadTrack", "LoadTrackState.Unload")
                    LoadTrackState.Unload
                }
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
                val musicPlayerRepository = application.container.musicPlayerRepository
                val downloadManagerImpl = application.container.downloadManagerImpl
                DownloadViewModel(musicPlayerRepository = musicPlayerRepository, downloadManagerImpl = downloadManagerImpl)
            }
        }
    }
}
