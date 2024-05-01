package com.example.musicplayer.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.Log
import androidx.navigation.NavController
import androidx.wear.activity.ConfirmationActivity
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.example.musicplayer.R
import com.example.musicplayer.data.datastore.AuthStatus
import com.example.musicplayer.data.datastore.DataStoreManager
import com.example.musicplayer.data.datastore.MyDataStore
import com.example.musicplayer.ui.components.MenuItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.wait

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MenuScreen(navController: NavController, username: String? = "") {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                contentPadding = PaddingValues(
                    top = 10.dp,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 10.dp
                ),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                columns = GridCells.Fixed(2),
            ) {
                item { MenuItem("Поиск", R.drawable.search, navController, "song_search") }
                item { MenuItem("Любимое", R.drawable.heart_outline, navController, "like_music") }
                item {
                    MenuItem(
                        "Плейлисты",
                        R.drawable.playlist_music_outline,
                        navController,
                        "playlists"
                    )
                }
                item {
                    MenuItem(
                        "Скачанное",
                        R.drawable.tray_arrow_down,
                        navController,
                        "load_musics"
                    )
                }
            }
        }
    }

    GlobalScope.launch  {
        DataStoreManager.getInstance().isCompleted.collect { isCompleted ->
            if (isCompleted) {
                DataStoreManager.getInstance().isShow.collect { isShow ->
                    if (!isShow) {
                        val activity = context as Activity
                        val intent = Intent(activity, ConfirmationActivity::class.java).apply {
                            putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION)
                            putExtra(ConfirmationActivity.EXTRA_MESSAGE, username)
                            putExtra(ConfirmationActivity.EXTRA_ANIMATION_DURATION_MILLIS, 2000)
                        }
                        activity.startActivity(intent)
                        DataStoreManager.getInstance().updateIsShow(true)
                    }
                }
            }
        }
    }
}


