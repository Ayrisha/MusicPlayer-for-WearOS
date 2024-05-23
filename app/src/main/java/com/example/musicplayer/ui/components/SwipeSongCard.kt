package com.example.musicplayer.ui.components

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.animate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.RevealState
import androidx.wear.compose.foundation.RevealValue
import androidx.wear.compose.foundation.rememberRevealState
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.SwipeToRevealActionColors
import androidx.wear.compose.material.SwipeToRevealChip
import androidx.wear.compose.material.SwipeToRevealDefaults
import com.example.musicplayer.data.model.Track
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.viewModel.LikeViewModel
import com.example.musicplayer.ui.viewModel.PlaylistTracksViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@ExperimentalFoundationApi
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalWearFoundationApi::class, ExperimentalWearMaterialApi::class)
@Composable
fun SwipeSongCard(
    index: Int,
    mediaController: MediaController,
    list: List<Track>,
    navController: NavController,
    id: String?,
    title: String,
    artist: String?,
    img: String?,
    onSwipe: () -> Unit,
    onClick: () -> Unit
){
    val mediaManager = MediaManager(mediaController)

    val coroutineScope = rememberCoroutineScope()

    val revealState = rememberRevealState()

    SwipeToRevealChip(
        action = SwipeToRevealDefaults.action(
            icon = { Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete") },
            label = { Text(text = "Удалить") },
            onClick = {
                coroutineScope.launch {
                    delay(3000)
                    onSwipe()
                    revealState.animateTo(RevealValue.Covered)
                }
            }),
        revealState = revealState,
        undoAction = SwipeToRevealDefaults.undoAction(
            label = { Text(text = "Отмена", color = Color.White) },
            onClick = {coroutineScope.cancel()}),
        colors = SwipeToRevealActionColors(
            actionBackgroundColor = Color(238,103,92),
            actionContentColor = Color.Black,
            additionalActionBackgroundColor = Color.White,
            additionalActionContentColor = Color.White,
            undoActionBackgroundColor = Color(32,33,36),
            undoActionContentColor = Color.White
        )
    ){
        SongCard(
            id = id,
            title = title,
            artist = artist,
            img = "http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/image/$img.png",
            onClick = {
                mediaManager.setMediaItems(list, index)
                onClick()
            },
            onLongClick = {
                navController.navigate("song_info/${title}/${artist}/${img}/${id}")
            },
            mediaController = mediaController
        )
    }
}