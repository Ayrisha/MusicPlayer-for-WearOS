package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.musicplayer.datastore.DataStoreManager
import com.example.musicplayer.media.PlaybackService
import com.example.musicplayer.ui.screens.AddPlayListScreen
import com.example.musicplayer.ui.screens.AuthScreen
import com.example.musicplayer.ui.screens.LikeScreen
import com.example.musicplayer.ui.screens.LoadMusicScreen
import com.example.musicplayer.ui.screens.MenuScreen
import com.example.musicplayer.ui.screens.PlayListTracksScreen
import com.example.musicplayer.ui.screens.PlayScreen
import com.example.musicplayer.ui.screens.PlaylistScreen
import com.example.musicplayer.ui.screens.SearchBarScreen
import com.example.musicplayer.ui.screens.SearchScreen
import com.example.musicplayer.ui.viewModel.PlayerViewModel
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.launch

@UnstableApi
class MainActivity : ComponentActivity() {
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var mediaController: MediaController

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var startScreen = "AuthScreen"

        val sessionToken =
            SessionToken(this, ComponentName(this, PlaybackService::class.java))

        controllerFuture =
            MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({
            mediaController = controllerFuture.get()
        }, MoreExecutors.directExecutor())

        lifecycleScope.launch {
            DataStoreManager.getInstance().isCompleted.collect { isCompleted ->
                startScreen = if (isCompleted) {
                    Log.d("Data", "True")
                    "menu_screen"
                } else {
                    Log.d("Data", "False")
                    "AuthScreen"
                }
            }
        }

        setContent {
            val navController = rememberSwipeDismissableNavController()
            SwipeDismissableNavHost(navController = navController, startDestination = "auth"){
                navigation(
                    startDestination = "AuthScreen",
                    route = "auth"
                ){
                    composable("AuthScreen") {
                        AuthScreen(navController, "menu_screen")
                    }
                }
                navigation(
                    startDestination = "menu_screen/{name}",
                    route = "menu"
                ) {
                    composable("menu_screen") {
                        MenuScreen(navController)
                    }
                    composable(
                        route = "menu_screen/{name}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType }
                        )
                    ) {
                        val name = it.arguments?.getString("name")
                        MenuScreen(navController, name)
                    }
                    composable("song_search") {
                        SearchScreen(mediaController, navController)
                    }
                    composable("searchscreen") {
                        SearchBarScreen(navController)
                    }
                    composable("like_music") {
                        LikeScreen(mediaController, navController)
                    }
                    composable("playlists/{trackId}",
                        arguments = listOf(
                            navArgument("trackId") { type = NavType.StringType }
                        )) {
                        val trackId = it.arguments?.getString("trackId")
                        PlaylistScreen(navController, trackId)
                    }
                    composable("playlists") {
                        PlaylistScreen(navController)
                    }
                    composable("addplaylist") {
                        AddPlayListScreen(navController)
                    }
                    composable(
                        "playlisttracks/{playlistname}",
                        arguments = listOf(
                            navArgument("playlistname") {type = NavType.StringType}
                        )
                    ){
                        val playlistname = it.arguments?.getString("playlistname")
                        playlistname?.let { it1 -> PlayListTracksScreen(it1, mediaController, navController) }
                    }
                    composable("load_musics") {
                        LoadMusicScreen()
                    }
                    composable(
                        route = "play_screen"
                    ) {
                        val playerViewModel = PlayerViewModel(mediaController)
                        PlayScreen(navController, mediaController, playerViewModel)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaController.isInitialized) {
            mediaController.release()
        }
    }
}



