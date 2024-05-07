package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.navigation.plusAssign
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.WearNavigator
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.musicplayer.data.datastore.MyDataStore
import com.example.musicplayer.data.PlaybackService
import com.example.musicplayer.data.datastore.DataStoreManager
import com.example.musicplayer.ui.screens.AuthScreen
import com.example.musicplayer.ui.screens.LikeScreen
import com.example.musicplayer.ui.screens.LoadMusicScreen
import com.example.musicplayer.ui.screens.MenuScreen
import com.example.musicplayer.ui.screens.PlayScreen
import com.example.musicplayer.ui.screens.PlaylistScreen
import com.example.musicplayer.ui.screens.SearchScreen
import com.example.musicplayer.ui.viewModel.LikeViewModel
import com.example.musicplayer.ui.viewModel.PlayerViewModel
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.internal.wait

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
                    composable("like_music") {
                        LikeScreen(mediaController, navController)
                    }
                    composable("playlists") {
                        PlaylistScreen()
                    }
                    composable("load_musics") {
                        LoadMusicScreen()
                    }
                    composable(
                        route = "play_screen"
                    ) {
                        val playerViewModel = PlayerViewModel(mediaController)
                        PlayScreen(mediaController, playerViewModel)
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        mediaController.release()
    }
}



