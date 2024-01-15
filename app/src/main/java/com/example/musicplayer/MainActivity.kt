package com.example.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.musicplayer.screens.LikeScreen
import com.example.musicplayer.screens.LoadMusicScreen
import com.example.musicplayer.screens.MenuScreen
import com.example.musicplayer.screens.PlayScreen
import com.example.musicplayer.screens.PlaylistScreen
import com.example.musicplayer.screens.SearchScreen

class MainActivity : ComponentActivity() {
  @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberSwipeDismissableNavController()

            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "menu_screen"
            ) {
                composable("menu_screen") {
                    MenuScreen(navController)
                }
                composable("song_search") {
                    SearchScreen()
                }
                composable("like_music") {
                    LikeScreen()
                }
                composable("playlists") {
                    PlaylistScreen()
                }
                composable("load_musics") {
                    LoadMusicScreen()
                }
                composable("play_screen") {
                    PlayScreen(applicationContext)
                }
            }
        }
    }
}


