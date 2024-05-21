package com.example.musicplayer.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.musicplayer.ui.components.add_playlist.AddTextField
import com.example.musicplayer.ui.viewModel.PlayListViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AddPlayListScreen(
    navController: NavController
) {
    val playListViewModel: PlayListViewModel = viewModel(factory = PlayListViewModel.Factory)
    val searchTextState = playListViewModel.getTextState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 10.dp,
                start = 20.dp,
                end = 20.dp,
                bottom = 10.dp
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Введите название плейлиста",
            textAlign = TextAlign.Center,
            color = Color.White
        )
        Spacer(modifier = Modifier.padding(4.dp))
        AddTextField(
            navController = navController,
            text = searchTextState.value,
            onTextChange = {
                playListViewModel.updateSearchTextState(it)
            },
            onSearchClicked = {
                playListViewModel.setPlaylists(it)
            }
        )

    }
}