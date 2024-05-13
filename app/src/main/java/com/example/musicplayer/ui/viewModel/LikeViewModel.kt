package com.example.musicplayer.ui.viewModel

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.ui.viewModel.state.TrackListState
import com.example.musicplayer.ui.viewModel.state.TrackUiState
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.io.IOException

class LikeViewModel (
    private val musicPlayerRepository: MusicPlayerRepository
): ViewModel(){
    var likeUiState: TrackListState by mutableStateOf(TrackListState.Loading)

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getTracksLike(){
        viewModelScope.launch {
            TrackListState.Loading
            kotlinx.coroutines.delay(2000)
            likeUiState = try {
                val listLikes = musicPlayerRepository.getTracksLike()
                if (listLikes.isEmpty()){
                    TrackListState.Empty
                }
                else{
                    TrackListState.Success(tracks = listLikes.asReversed())
                }
            } catch (e: HttpException){
                TrackListState.Error
            } catch (e: IOException){
                TrackListState.Error
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun deleteTrackLike(trackId: String){
        viewModelScope.launch {
            musicPlayerRepository.deleteTrackLike(trackId)
            getTracksLike()
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MusicApplication)
                val musicRepository = application.container.musicPlayerRepository
                LikeViewModel(musicPlayerRepository = musicRepository)
            }
        }
    }
}