package com.example.musicplayer.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.musicplayer.ui.components.SearchBar
import com.example.musicplayer.ui.viewModel.SearchViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SearchBarScreen(
    navController: NavController
){
    val trackViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)

    val searchTextState = trackViewModel.searchTextState

    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        SearchBar(
            text = searchTextState.value,
            onTextChange = {
                trackViewModel.updateSearchTextState(it)
            },
            onSearchClicked = {
                trackViewModel.searchTrack(it)
                navController.previousBackStackEntry?.savedStateHandle?.set("search_query", it)
            },
            keyboardController = keyboardController
        )
    }
}