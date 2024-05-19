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
                Log.d("RecentSearchViewModel", "Collected searches from DataStore: ${_recentSearches.value}")
                updateRecentSearchState( _recentSearches.value)
            }
        }
    }

    private fun updateRecentSearchState(searches: List<String>) {
        Log.d("RecentSearchViewModel", "RecentSearchState: $searches")
        recentSearchState = if (searches.isEmpty()) {
            Log.d("RecentSearchViewModel", "RecentSearchState: empty")
            RecentSearchState.Empty
        } else {
            Log.d("RecentSearchViewModel", "RecentSearchState: NotEmpty")
            RecentSearchState.Success(searches)
        }
    }

    fun updateSearchTextState(newText: String) {
        _textState.value = newText
    }

    fun setSearch(query: String) {
        if (query.isBlank()) return
        Log.d("RecentSearchViewModel", "SetSearch: $query")

        viewModelScope.launch {
            Log.d("RecentSearchViewModel", "Current searches before addition: ${_recentSearches.value}")

            if (!_recentSearches.value.contains(query)) {
                val updatedSearches = _recentSearches.value.toMutableList()
                updatedSearches.add(0, query)

                Log.d("RecentSearchViewModel", "Updated searches after addition: $updatedSearches")

                if (updatedSearches.size > 5) {
                    updatedSearches.removeAt(updatedSearches.size - 1)
                }

                _recentSearches.value = updatedSearches

                Log.d("RecentSearchViewModel", "Final searches to be saved: $updatedSearches")

                myDataStore.updateRecentSearches(updatedSearches)

                myDataStore.recentSearches.collect { searches ->
                    val savedSearches = searches.joinToString(", ")
                    Log.d("RecentSearchViewModel", "Saved searches after update: $savedSearches")
                }
            } else {
                Log.d("RecentSearchViewModel", "Query already exists: $query")
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