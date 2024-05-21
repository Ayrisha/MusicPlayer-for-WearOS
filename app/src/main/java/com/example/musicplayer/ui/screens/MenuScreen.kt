package com.example.musicplayer.ui.screens

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.scrollAway
import com.example.musicplayer.auth.GoogleSignInHelper
import com.example.musicplayer.ui.components.menu.MenuHeader
import com.example.musicplayer.ui.components.menu.MenuItem
import com.example.musicplayer.ui.components.menu.SignInOutButton
import com.example.musicplayer.ui.components.menu.UserEmail
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.rotaryinput.ScalingLazyColumnRotaryScrollAdapter
import com.google.android.horologist.compose.rotaryinput.rotaryWithSnap
import kotlinx.coroutines.launch

@OptIn(ExperimentalHorologistApi::class, ExperimentalWearFoundationApi::class)
@Composable
fun MenuScreen(navController: NavController) {
    val context = LocalContext.current

    val listState = rememberScalingLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val rotaryScrollAdapter = ScalingLazyColumnRotaryScrollAdapter(listState)
    val focusRequester = rememberActiveFocusRequester()

    val googleSignInHelper = remember { GoogleSignInHelper(context) }
    val isUserSignedIn = remember { mutableStateOf(googleSignInHelper.isUserSignedIn()) }
    val email = remember { googleSignInHelper.getEmail() }

    val menuItems = remember {
        listOf(
            "Поиск" to "song_search",
            "Плейлисты" to "playlists",
            "Избранное" to "like_music",
            "Скачанное" to "load_musics"
        )
    }

    Scaffold(
        positionIndicator = { PositionIndicator(listState) },
        timeText = { TimeText(modifier = Modifier.scrollAway(listState)) }
    ) {
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .onRotaryScrollEvent {
                    coroutineScope.launch {
                        listState.scrollBy(it.verticalScrollPixels)
                        listState.animateScrollBy(0f)
                    }
                    true
                }
                .rotaryWithSnap(rotaryScrollAdapter = rotaryScrollAdapter)
                .focusRequester(focusRequester)
                .focusable(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { MenuHeader() }
            items(menuItems) { (title, route) ->
                MenuItem(title, navController, route)
            }
            item { SignInOutButton(navController, isUserSignedIn, googleSignInHelper) }
            item { UserEmail(email, isUserSignedIn) }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}


