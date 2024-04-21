package com.example.musicplayer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import com.example.musicplayer.R
import com.example.musicplayer.ui.components.MenuItem
import com.example.musicplayer.ui.components.MinimalDialog
import kotlinx.coroutines.delay

@Composable
fun MenuScreen(navController: NavController, showDialog: Boolean? = false, username: String? = "") {
    val listState = rememberScalingLazyListState()

    var showDialogState by remember { mutableStateOf(showDialog) }

    Scaffold(
        timeText = {
            TimeText(modifier = Modifier.scrollAway(listState))
        },
        vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                contentPadding = PaddingValues(
                    top = 20.dp,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                columns = GridCells.Fixed(2),
            ) {
                item { MenuItem("Поиск", R.drawable.search, navController, "song_search") }
                item { MenuItem("Любимое", R.drawable.heart_outline, navController, "AuthScreen") }
                item {
                    MenuItem(
                        "Плейлисты",
                        R.drawable.playlist_music_outline,
                        navController,
                        "playlists"
                    )
                }
                item {
                    MenuItem(
                        "Скачанное",
                        R.drawable.tray_arrow_down,
                        navController,
                        "load_musics"
                    )
                }
            }
        }
    }

    if (showDialogState == true) {
        LaunchedEffect(Unit) {
            delay(5000)
            showDialogState = false
        }
        MinimalDialog(onDismissRequest = { showDialogState = false }, text = "$username\n Вход выполнен")
    }
}


