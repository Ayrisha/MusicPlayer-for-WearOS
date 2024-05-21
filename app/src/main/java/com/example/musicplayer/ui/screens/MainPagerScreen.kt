package com.example.musicplayer.ui.screens

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import com.example.musicplayer.ui.viewModel.PlayerViewModel

sealed class Page {
    object Menu : Page()
    object Player : Page()
}

val pages = listOf(
    Page.Menu, Page.Player
)

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@androidx.annotation.OptIn(UnstableApi::class) @OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainPagerScreen(
    navController: NavController,
    mediaController: MediaController,
    playerViewModel: PlayerViewModel
) {
    val pagerState = rememberPagerState() { 2 }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            modifier = Modifier.weight(1f),
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
                PagerDefaults.pageNestedScrollConnection(pagerState, Orientation.Horizontal)
            }
        ) { pageIndex ->
            when (pages[pageIndex]) {
                Page.Menu -> MenuScreen(navController = navController)
                Page.Player -> PlayScreen(
                    navController = navController,
                    controller = mediaController,
                    playerViewModel = playerViewModel
                )
            }
        }
    }
}
