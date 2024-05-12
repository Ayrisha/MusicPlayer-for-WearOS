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
import kotlinx.coroutines.launch
import java.io.IOException

class PlaylistTracksViewModel (
    private val musicPlayerRepository: MusicPlayerRepository
): ViewModel(){
    var likeUiState: TrackListState by mutableStateOf(TrackListState.Loading)

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getTracks(title:String){
        viewModelScope.launch {
            likeUiState = try {
                val listLikes = musicPlayerRepository.getPlayListTracks(title)
                if (listLikes.isEmpty()){
                    TrackListState.Empty
                }
                else{
                    TrackListState.Success(tracks = listLikes)
                }
            } catch (e: IOException){
                TrackListState.Error
            } catch (e: HttpException){
                TrackListState.Error
            }
        }
    }

    fun setTrack(title:String, trackId: String){
        viewModelScope.launch {
            musicPlayerRepository.setPlayListTrack(title, trackId)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun deleteTrack(title:String, trackId: String){
        viewModelScope.launch {
            musicPlayerRepository.deletePlayListTrack(title, trackId)
            getTracks(title)
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MusicApplication)
                val musicRepository = application.container.musicPlayerRepository
                PlaylistTracksViewModel(musicPlayerRepository = musicRepository)
            }
        }
    }
}