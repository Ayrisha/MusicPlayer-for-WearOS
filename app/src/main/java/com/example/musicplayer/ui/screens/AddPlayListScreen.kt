package com.example.musicplayer.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.musicplayer.ui.components.AddTextField
import com.example.musicplayer.ui.viewModel.PlayListViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AddPlayListScreen(
    navController: NavController
) {
    val playListViewModel: PlayListViewModel = viewModel(factory = PlayListViewModel.Factory)

    val searchTextState = playListViewModel.getTextState

    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 10.dp,
                start = 20.dp,
                end = 20.dp,
                bottom = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        AddTextField(
            navController = navController,
            text = searchTextState.value,
            onTextChange = {
                playListViewModel.updateSearchTextState(it)
            },
            onSearchClicked = {
                playListViewModel.setPlaylists(it)
            },
            keyboardController = keyboardController
        )

    }
}