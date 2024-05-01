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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
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
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.internal.wait

@UnstableApi
class MainActivity : ComponentActivity() {
    lateinit var controllerFuture: ListenableFuture<MediaController>
    @SuppressLint("CoroutineCreationDuringComposition")
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var mediaController: MediaController
        var startScreen = "AuthScreen"

        val sessionToken =
            SessionToken(this, ComponentName(this, PlaybackService::class.java))

        controllerFuture =
            MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({
            mediaController = controllerFuture.get()
        }, MoreExecutors.directExecutor())

        lifecycleScope.launch  {
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
                    route = "menu_screen/{name}",
                    arguments = listOf(
                        navArgument("name") { type = NavType.StringType }
                    )
                ) {
                    val name = it.arguments?.getString("name")
                    MenuScreen(navController, name)
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
                    route = "play_screen/{id}/{title}/{artist}",
                    arguments = listOf(
                        navArgument("id") { type = NavType.StringType },
                        navArgument("title") { type = NavType.StringType },
                        navArgument("artist") { type = NavType.StringType })
                ) {
                    val id = it.arguments?.getString("id")
                    val title = it.arguments?.getString("title")
                    val artist = it.arguments?.getString("artist")
                    if (id != null) {
                        PlayScreen(mediaController, id, title, artist)
                    }
                }
            }
        }
    }
}



