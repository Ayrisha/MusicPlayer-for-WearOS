package com.example.musicplayer.ui.viewModel

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.data.Track
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface TrackUiState{
    data class Success(val trackSearches: List<Track>) : TrackUiState
    data class Start(val trackPopular: List<Track>) : TrackUiState
    object Empty : TrackUiState
    object Error : TrackUiState
    object Loading : TrackUiState
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class TrackViewModel (
    private val musicPlayerRepository: MusicPlayerRepository
): ViewModel(){

    var trackUiState: TrackUiState by mutableStateOf(TrackUiState.Loading)

    private val _searchTextState: MutableState<String> =
        mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    fun popularTrack(){
        viewModelScope.launch {
            trackUiState = try {
                val listPopularTrack = musicPlayerRepository.popularTrack()
                if (listPopularTrack.isEmpty()){
                    TrackUiState.Error
                }
                else{
                    TrackUiState.Start(trackPopular = listPopularTrack)
                }
            } catch (e: IOException){
                TrackUiState.Error
            } catch (e: HttpException){
                TrackUiState.Error
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun searchTrack(title: String){
        viewModelScope.launch {
            trackUiState = try {
                val listTrack = musicPlayerRepository.searchTrack(title)
                if (listTrack.isEmpty()){
                    TrackUiState.Empty
                }
                else{
                    TrackUiState.Success(trackSearches = listTrack)
                }
            } catch (e: IOException){
                TrackUiState.Error
            } catch (e: HttpException){
                TrackUiState.Error
            }
        }
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MusicApplication)
                val musicRepository = application.container.musicPlayerRepository
                TrackViewModel(musicPlayerRepository = musicRepository)
            }
        }
    }
}