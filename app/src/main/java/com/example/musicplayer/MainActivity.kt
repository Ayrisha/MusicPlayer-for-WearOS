package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.ComponentName
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.wear.compose.material.HorizontalPageIndicator
import androidx.wear.compose.material.PageIndicatorState
import androidx.wear.compose.material.PageIndicatorStyle
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.media.PlaybackService
import com.example.musicplayer.ui.screens.AddPlayListScreen
import com.example.musicplayer.ui.screens.AuthInfo
import com.example.musicplayer.ui.screens.AuthScreen
import com.example.musicplayer.ui.screens.LikeScreen
import com.example.musicplayer.ui.screens.LoadMusicScreen
import com.example.musicplayer.ui.screens.MenuScreen
import com.example.musicplayer.ui.screens.NavigationDestinations
import com.example.musicplayer.ui.screens.Page
import com.example.musicplayer.ui.screens.PlayListTracksScreen
import com.example.musicplayer.ui.screens.PlayScreen
import com.example.musicplayer.ui.screens.PlaylistScreen
import com.example.musicplayer.ui.screens.Routes
import com.example.musicplayer.ui.screens.SearchBarScreen
import com.example.musicplayer.ui.screens.SearchScreen
import com.example.musicplayer.ui.screens.SongInfoScreen
import com.example.musicplayer.ui.viewModel.PlayerViewModel
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@UnstableApi
class MainActivity : ComponentActivity() {
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var mediaController: MediaController

    @OptIn(ExperimentalFoundationApi::class)
    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = applicationContext as MusicApplication
        val dataStore = application.container.dataStore

        val context = this

        val sessionToken =
            SessionToken(this, ComponentName(this, PlaybackService::class.java))

        runBlocking {
            controllerFuture =
                MediaController.Builder(context, sessionToken).buildAsync()
            controllerFuture.addListener({
                mediaController = controllerFuture.get()
            }, MoreExecutors.directExecutor())
        }


        setContent {
            val pagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0F,
                pageCount = { 2 }
            )

            val pages = listOf(
                Page.Menu,
                Page.Player
            )

            val pageIndicatorState = remember {
                object : PageIndicatorState {
                    override val pageCount: Int
                        get() = pagerState.pageCount
                    override val selectedPage: Int
                        get() = pagerState.currentPage
                    override val pageOffset: Float
                        get() = pagerState.currentPageOffsetFraction
                }
            }
            val coroutineScope = rememberCoroutineScope()

            val navController = rememberSwipeDismissableNavController()
            val playerNavController = rememberSwipeDismissableNavController()

            LaunchedEffect(Unit) {
                val refreshToken = dataStore.refreshToken.firstOrNull()

                if (refreshToken == null) {
                    navController.navigate(NavigationDestinations.Auth)
                } else {
                    navController.navigate(NavigationDestinations.Menu)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                HorizontalPager(
                    modifier = Modifier
                        .weight(1f),
                    state = pagerState,
                    pageSpacing = 0.dp,
                    userScrollEnabled = true,
                    reverseLayout = false,
                    contentPadding = PaddingValues(0.dp),
                    beyondBoundsPageCount = 0,
                    pageSize = PageSize.Fill,
                    flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                    key = null,
                    pageNestedScrollConnection = remember(pagerState) {
                        PagerDefaults.pageNestedScrollConnection(
                            pagerState,
                            Orientation.Horizontal
                        )
                    }
                ) { pageIndex ->
                    when (pages[pageIndex]) {
                        Page.Menu -> {
                            SwipeDismissableNavHost(
                                modifier = Modifier
                                    .pointerInput(Unit) {
                                        detectHorizontalDragGestures { _, dragAmount ->
                                            val currentRoute =
                                                navController.currentBackStackEntry?.destination?.route
                                            if (dragAmount < -30) {
                                                coroutineScope.launch {
                                                    pagerState.animateScrollToPage(1)
                                                }
                                            } else if (dragAmount > 30) {
                                                if (currentRoute.equals(Routes.MenuScreen)) {
                                                    finish()
                                                } else {
                                                    navController.popBackStack()
                                                }
                                            }
                                        }
                                    },
                                navController = navController,
                                startDestination = NavigationDestinations.Menu
                            ) {
                                navigation(
                                    startDestination = Routes.AuthScreen,
                                    route = NavigationDestinations.Auth
                                ) {
                                    composable(Routes.AuthScreen) {
                                        AuthScreen(navController)
                                    }
                                    composable(Routes.AuthInfo) {
                                        AuthInfo(navController)
                                    }
                                }
                                navigation(
                                    startDestination = Routes.MenuScreen,
                                    route = NavigationDestinations.Menu
                                ) {
                                    composable(Routes.MenuScreen) {
                                        MenuScreen(navController)
                                    }
                                    composable(Routes.SearchScreen) {
                                        SearchScreen(mediaController, navController, pagerState)
                                    }
                                    composable(Routes.SearchBarScreen) {
                                        SearchBarScreen(navController)
                                    }
                                    composable(Routes.LikeScreen) {
                                        LikeScreen(
                                            mediaController,
                                            navController,
                                            pagerState,
                                            MediaManager(mediaController)
                                        )
                                    }
                                    composable(
                                        Routes.PlaylistScreen + "/{trackId}",
                                        arguments = listOf(navArgument("trackId") {
                                            type = NavType.StringType
                                        })
                                    ) {
                                        val trackId = it.arguments?.getString("trackId")
                                        PlaylistScreen(navController, trackId)
                                    }
                                    composable(Routes.PlaylistScreen) {
                                        PlaylistScreen(navController)
                                    }
                                    composable(Routes.AddPlayListScreen) {
                                        AddPlayListScreen(navController)
                                    }
                                    composable(
                                        Routes.PlayListTracksScreen + "/{playlistname}",
                                        arguments = listOf(navArgument("playlistname") {
                                            type = NavType.StringType
                                        })
                                    ) {
                                        val playlistname = it.arguments?.getString("playlistname")
                                        playlistname?.let {
                                            PlayListTracksScreen(
                                                it,
                                                mediaController,
                                                navController,
                                                pagerState
                                            )
                                        }
                                    }
                                    composable(Routes.LoadMusicScreen) {
                                        LoadMusicScreen(
                                            mediaController,
                                            LocalContext.current,
                                            pagerState,
                                            navController
                                        )
                                    }
                                    composable(
                                        route = Routes.SongInfoScreen + "/{title}/{artist}/{id}/{img}?playlist={playlist}",
                                        arguments = listOf(
                                            navArgument("title") { type = NavType.StringType },
                                            navArgument("artist") { type = NavType.StringType },
                                            navArgument("img") { type = NavType.StringType },
                                            navArgument("id") { type = NavType.StringType },
                                            navArgument("playlist") {
                                                type = NavType.StringType; defaultValue = ""
                                            }
                                        )
                                    ) {
                                        val title = it.arguments?.getString("title")
                                        val artist = it.arguments?.getString("artist")
                                        val img = Uri.decode(it.arguments?.getString("img"))
                                        val id = it.arguments?.getString("id")
                                        val playlist = it.arguments?.getString("playlist")

                                        if (title != null) {
                                            SongInfoScreen(
                                                id,
                                                title,
                                                artist,
                                                img,
                                                mediaController,
                                                navController,
                                                pagerState,
                                                playlist
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Page.Player -> {
                            val playerViewModel = PlayerViewModel(mediaController, context = context)
                            PlayScreen(playerViewModel, playerNavController, mediaController)
                        }
                    }
                }
            }

            HorizontalPageIndicator(
                pageIndicatorState = pageIndicatorState,
                indicatorStyle = PageIndicatorStyle.Curved
            )
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (::mediaController.isInitialized) {
            mediaController.release()
        }
    }
}





