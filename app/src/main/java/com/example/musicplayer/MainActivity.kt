package com.example.musicplayer

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.musicplayer.data.AppPreferences
import com.example.musicplayer.data.ExoPlayerObject
import com.example.musicplayer.ui.screens.AuthScreen
import com.example.musicplayer.ui.screens.LikeScreen
import com.example.musicplayer.ui.screens.LoadMusicScreen
import com.example.musicplayer.ui.screens.MenuScreen
import com.example.musicplayer.ui.screens.PlayScreen
import com.example.musicplayer.ui.screens.PlaylistScreen
import com.example.musicplayer.ui.screens.SearchScreen

class MainActivity : ComponentActivity() {
    @UnstableApi
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppPreferences.isLogin = false

        val startScreen: String = if (AppPreferences.isLogin){
            "menu_screen"
        } else{
            "AuthScreen"
        }

        setContent {
            val navController = rememberSwipeDismissableNavController()

            SwipeDismissableNavHost(
                navController = navController,
                startDestination = startScreen
            ) {
                composable("AuthScreen") {
                    AuthScreen(navController, "menu_screen")
                }
                composable("menu_screen") {
                    MenuScreen(navController)
                }
                composable(
                    route = "menu_screen/{state}/{name}",
                    arguments = listOf(
                        navArgument("state") { type = NavType.BoolType },
                        navArgument("name") { type = NavType.StringType }
                    )
                ) {
                    val state = it.arguments?.getBoolean("state")
                    val name = it.arguments?.getString("name")
                    MenuScreen(navController, state, name)
                }
                composable("song_search") {
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
                composable(
                    route ="play_screen/{id}/{title}/{artist}",
                    arguments = listOf(
                        navArgument("id") { type = NavType.StringType },
                        navArgument("title") { type = NavType.StringType },
                        navArgument("artist") { type = NavType.StringType })
                ) {
                    val id = it.arguments?.getString("id")
                    val title = it.arguments?.getString("title")
                    val artist = it.arguments?.getString("artist")
                    if (id != null) {
                        PlayScreen(id, title, artist)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        ExoPlayerObject.destroy()
    }
}



