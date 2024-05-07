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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.data.model.PlayList
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface PlayListUiState{
    data class Success(val plaLists: List<PlayList>) : PlayListUiState
    object Empty : PlayListUiState
    object Error : PlayListUiState
    object Loading : PlayListUiState
}

class PlayListViewModel(
    private val musicPlayerRepository: MusicPlayerRepository
): ViewModel() {

    var playListUiState: PlayListUiState by mutableStateOf(PlayListUiState.Loading)

    private val _getTextState: MutableState<String> =
        mutableStateOf(value = "")
    val getTextState: State<String> = _getTextState

    fun updateSearchTextState(newValue: String) {
        _getTextState.value = newValue
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getPlaylists(){
        viewModelScope.launch {
            playListUiState = try {
                val listPlayList = musicPlayerRepository.getPlayList()
                if (listPlayList.isEmpty()){
                    PlayListUiState.Error
                }
                else{
                    PlayListUiState.Success(plaLists = listPlayList)
                }
            } catch (e: IOException){
                PlayListUiState.Error
            } catch (e: HttpException){
                PlayListUiState.Error
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun setPlaylists(title: String){
        viewModelScope.launch {
            musicPlayerRepository.setPlayList(title)
            getPlaylists()
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MusicApplication)
                val musicRepository = application.container.musicPlayerRepository
                PlayListViewModel(musicPlayerRepository = musicRepository)
            }
        }
    }
}