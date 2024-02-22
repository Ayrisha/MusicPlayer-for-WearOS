package com.example.musicplayer

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.annotation.RequiresExtension
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.musicplayer.ui.screens.LikeScreen
import com.example.musicplayer.ui.screens.LoadMusicScreen
import com.example.musicplayer.ui.screens.MenuScreen
import com.example.musicplayer.ui.screens.PlayScreen
import com.example.musicplayer.ui.screens.PlaylistScreen
import com.example.musicplayer.ui.screens.SearchScreen

class MainActivity : ComponentActivity() {
  @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
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
                composable("song_search"){
                    SearchScreen(navController)
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
                composable("play_screen/{id}", listOf(navArgument("id"){type = NavType.StringType})) {
                    val id = it.arguments?.getString("id")
                    if (id != null) {
                        PlayScreen(applicationContext, id)
                    }
                }
            }
        }
    }
}


