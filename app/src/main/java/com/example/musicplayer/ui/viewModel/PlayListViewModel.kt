package com.example.musicplayer.ui.viewModel

import android.os.Build
import android.util.Log
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
import com.example.musicplayer.ui.viewModel.state.PlayListUiState
import com.example.musicplayer.ui.viewModel.state.TrackListState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

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
    fun setPlaylists(title: String){
        viewModelScope.launch {
            musicPlayerRepository.setPlayList(title)
            getPlaylists()
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getPlaylists(){
        viewModelScope.launch {
            PlayListUiState.Loading
            playListUiState = try {
                val listPlayList = musicPlayerRepository.getPlayList()
                if (listPlayList.isEmpty()){
                    PlayListUiState.Empty
                }
                else{
                    PlayListUiState.Success(plaLists = listPlayList)
                }
            } catch (e: HttpException){
                if (e.message == "HTTP 401 "){
                    PlayListUiState.NotRegister
                }
                else{
                    PlayListUiState.Error
                }
            } catch (e: IOException){
                PlayListUiState.Error
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun deletePlaylist(title: String){
        viewModelScope.launch {
            musicPlayerRepository.deletePlayList(title)
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