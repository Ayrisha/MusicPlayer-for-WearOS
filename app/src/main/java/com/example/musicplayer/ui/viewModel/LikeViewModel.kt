package com.example.musicplayer.ui.viewModel

import android.os.Build
import android.util.Log
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
import com.example.musicplayer.ui.viewModel.state.LikeState
import com.example.musicplayer.ui.viewModel.state.TrackListState
import com.example.musicplayer.ui.viewModel.state.TrackUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException

class LikeViewModel (
    private val musicPlayerRepository: MusicPlayerRepository
): ViewModel(){
    var likeUiState: TrackListState by mutableStateOf(TrackListState.Loading)

    var likeState: LikeState by mutableStateOf(LikeState.Dislike)

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
                if (e.message == "HTTP 401 "){
                    TrackListState.NotRegister
                }
                else{
                    TrackListState.Error
                }
            } catch (e: IOException){
                TrackListState.Error
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

    fun setLike(idMedia: String){
        viewModelScope.launch {
            try {
            musicPlayerRepository.setTrackLike(idMedia)
            checkLikeTrack(idMedia)
            } catch (_: Exception){
            }

        }
    }

    fun deleteLike(idMedia: String){
        viewModelScope.launch {
            try {
            musicPlayerRepository.deleteTrackLike(idMedia)
            checkLikeTrack(idMedia)
            } catch (_: Exception){}
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