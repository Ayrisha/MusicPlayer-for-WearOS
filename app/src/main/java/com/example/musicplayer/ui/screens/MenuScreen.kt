package com.example.musicplayer.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import com.example.musicplayer.R
import com.example.musicplayer.ui.components.MenuItem

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MenuScreen(navController: NavController, username: String? = "") {

    Scaffold(
        timeText = { TimeText() }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                contentPadding = PaddingValues(
                    top = 10.dp,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 10.dp
                ),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                columns = GridCells.Fixed(2),
            ) {
                item { MenuItem("Поиск", R.drawable.search, navController, "song_search") }
                item { MenuItem("Любимое", R.drawable.heart_outline, navController, "like_music") }
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
}


