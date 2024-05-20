package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
import com.example.musicplayer.auth.TokenAuthenticator
import com.example.musicplayer.media.PlaybackService
import com.example.musicplayer.ui.components.ConfirmationCard
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@UnstableApi
class MainActivity : ComponentActivity() {
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var mediaController: MediaController

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = applicationContext as MusicApplication
        val dataStore = application.container.dataStore

        val sessionToken =
            SessionToken(this, ComponentName(this, PlaybackService::class.java))

        controllerFuture =
            MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({
            mediaController = controllerFuture.get()
        }, MoreExecutors.directExecutor())

        setContent {
            val navController = rememberSwipeDismissableNavController()
            val requiresAuth by application.container.tokenAuthenticator.requiresAuthFlow.collectAsState()

            Log.d("MainActivity", "$requiresAuth")

            LaunchedEffect(requiresAuth) {
                if (requiresAuth) {
                    navController.navigate("auth") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }

            LaunchedEffect(Unit) {
                val refreshToken = runBlocking { dataStore.refreshToken.firstOrNull() }

                Log.d("MainActivity", "Get refresh token from Datastore: $refreshToken")

                if (refreshToken == null) {
                    navController.navigate("auth") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                } else {
                    val accessToken = runBlocking { dataStore.accessToken.firstOrNull() }

                    Log.d("MainActivity", "Get access token from Datastore: $accessToken")

                        navController.navigate("menu") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                }
            }

            SwipeDismissableNavHost(navController = navController, startDestination = "menu"){
                navigation(
                    startDestination = "AuthScreen",
                    route = "auth"
                ){
                    composable("AuthScreen") {
                        AuthScreen(navController)
                    }
                    composable(
                        "confirmation/{displayName}/{email}/{photoUrl}/{idToken}",
                        arguments = listOf(
                            navArgument("displayName") { type = NavType.StringType },
                            navArgument("email") { type = NavType.StringType },
                            navArgument("photoUrl") { type = NavType.StringType },
                            navArgument("idToken") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val displayName = backStackEntry.arguments?.getString("displayName")
                        val email = backStackEntry.arguments?.getString("email")
                        val photoUrl = backStackEntry.arguments?.getString("photoUrl")
                        val idToken = backStackEntry.arguments?.getString("idToken")
                        ConfirmationCard(navController, displayName, email, photoUrl, idToken)
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
                        LoadMusicScreen(context = LocalContext.current)
                    }
                    composable(
                        route = "play_screen"
                    ) {
                        val playerViewModel = PlayerViewModel(mediaController, context = LocalContext.current)
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



