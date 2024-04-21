package com.example.musicplayer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.musicplayer.ui.screens.AuthScreen
import com.example.musicplayer.ui.screens.LikeScreen
import com.example.musicplayer.ui.screens.LoadMusicScreen
import com.example.musicplayer.ui.screens.MenuScreen
import com.example.musicplayer.ui.screens.PlayScreen
import com.example.musicplayer.ui.screens.PlaylistScreen
import com.example.musicplayer.ui.screens.SearchScreen
import com.example.musicplayer.ui.screens.createVolumeViewModel
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.audio.ui.VolumeViewModel

class MainActivity : ComponentActivity() {
    private lateinit var player: ExoPlayer

    @SuppressLint("UnsafeOptInUsageError")
    private lateinit var playerViewModel: PlayerViewModel

    @kotlin.OptIn(ExperimentalHorologistApi::class)
    private lateinit var volumeViewModel: VolumeViewModel

    @UnstableApi
    @kotlin.OptIn(ExperimentalHorologistApi::class)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        player = ExoPlayer.Builder(this).build()
        playerViewModel = PlayerViewModel(player)
        volumeViewModel = createVolumeViewModel(this)

        setContent {
            val navController = rememberSwipeDismissableNavController()

            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "AuthScreen"
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
                    "play_screen/{id}",
                    listOf(navArgument("id") { type = NavType.StringType })
                ) {
                    val id = it.arguments?.getString("id")
                    if (id != null) {
                        PlayScreen(player, playerViewModel, volumeViewModel, applicationContext, id)
                    }
                }
            }
        }
    }
}



