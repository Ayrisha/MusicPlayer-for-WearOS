package com.example.musicplayer.ui.viewModel

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
import com.example.musicplayer.datastore.DataStoreManager
import com.example.musicplayer.ui.viewModel.state.RecentSearchState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RecentSearchViewModel(
    application: MusicApplication
) : ViewModel() {
    private val myDataStore = application.container.dataStore

    var recentSearchState by mutableStateOf<RecentSearchState>(RecentSearchState.Empty)
        private set

    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())

    private val _textState = mutableStateOf("")
    val getTextState: State<String> get() = _textState

    init {
        viewModelScope.launch {
            myDataStore.recentSearches.collect { searches ->
                _recentSearches.value = searches
                updateRecentSearchState(searches)
            }
        }
    }

    private fun updateRecentSearchState(searches: List<String>) {
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
        viewModelScope.launch {
            if (!_recentSearches.value.contains(query)) {
                val updatedSearches = _recentSearches.value.toMutableList()
                updatedSearches.add(0, query)
                if (updatedSearches.size > 5) {
                    updatedSearches.removeAt(updatedSearches.size - 1)
                }
                _recentSearches.value = updatedSearches
                myDataStore.updateRecentSearches(updatedSearches)
                updateRecentSearchState(updatedSearches)
            }
        }
    }

    fun removeRecentSearch(query: String) {
        viewModelScope.launch {
            val updatedSearches = _recentSearches.value.toMutableList()
            updatedSearches.remove(query)
            _recentSearches.value = updatedSearches
            myDataStore.updateRecentSearches(updatedSearches)
            updateRecentSearchState(updatedSearches)
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