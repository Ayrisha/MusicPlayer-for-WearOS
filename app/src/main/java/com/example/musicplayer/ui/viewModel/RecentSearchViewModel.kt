package com.example.musicplayer.ui.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.ui.viewModel.state.RecentSearchState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RecentSearchViewModel(
    application: MusicApplication
) : ViewModel() {
    private val myDataStore = application.container.dataStore

    var recentSearchState by mutableStateOf<RecentSearchState>(RecentSearchState.Empty)

    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())

    private val _textState = mutableStateOf("")
    val getTextState: State<String> get() = _textState

    init {
        viewModelScope.launch {
            myDataStore.recentSearches.collect { searches ->
                _recentSearches.value = searches.filter { it.isNotBlank() }
                updateRecentSearchState( _recentSearches.value)
            }
        }
    }

    fun updateRecentSearchState(searches: List<String>) {
        recentSearchState = if (searches.isEmpty()) {
            RecentSearchState.Empty
        } else {
            RecentSearchState.Success(searches)
        }
    }

    fun updateSearchTextState(newText: String) {
        _textState.value = newText
    }

    fun setSearch(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {

            if (!_recentSearches.value.contains(query)) {
                val updatedSearches = _recentSearches.value.toMutableList()
                updatedSearches.add(0, query)

                if (updatedSearches.size > 5) {
                    updatedSearches.removeAt(updatedSearches.size - 1)
                }

                _recentSearches.value = updatedSearches

                myDataStore.updateRecentSearches(updatedSearches)

                myDataStore.recentSearches.collect { searches ->
                    val savedSearches = searches.joinToString(", ")
                }
            } else {

            }
        }
    }

    fun removeRecentSearch(query: String) {
        viewModelScope.launch {
            val updatedSearches = _recentSearches.value.toMutableList()
            updatedSearches.remove(query)
            _recentSearches.value = updatedSearches
            myDataStore.updateRecentSearches(updatedSearches)
            updateRecentSearchState(_recentSearches.value)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MusicApplication)
                RecentSearchViewModel(application = application)
            }
        }
    }
}