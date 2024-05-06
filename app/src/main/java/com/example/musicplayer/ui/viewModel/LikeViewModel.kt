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
import com.example.musicplayer.data.Track
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.concurrent.Executor

sealed interface LikeUiState{
    data class Success(val tracksLike: List<Track>) : LikeUiState
    object Empty : LikeUiState
    object Error : LikeUiState
    object Loading : LikeUiState
}

class LikeViewModel (
    private val musicPlayerRepository: MusicPlayerRepository
): ViewModel(){
    var likeUiState: LikeUiState by mutableStateOf(LikeUiState.Loading)

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getTracksLike(){
        viewModelScope.launch {
            likeUiState = try {
                val listLikes = musicPlayerRepository.getTracksLike()
                if (listLikes.isEmpty()){
                    LikeUiState.Empty
                }
                else{
                    LikeUiState.Success(tracksLike = listLikes)
                }
            } catch (e: IOException){
                LikeUiState.Error
            } catch (e: HttpException){
                LikeUiState.Error
            }
        }
    }

    fun setLike(idMedia: String){
        viewModelScope.launch {
            musicPlayerRepository.setTrackLike(idMedia)
        }
    }

    fun deleteLike(idMedia: String){
        viewModelScope.launch {
            musicPlayerRepository.deleteTrackLike(idMedia)
        }
    }

    fun checkLike(idMedia: String):Boolean{
        var check: Boolean = false
        viewModelScope.launch {
            try{
                musicPlayerRepository.checkTrackLike(idMedia)
                check = true
            }catch (e: Exception){
                check = false
            }
        }
        return check
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