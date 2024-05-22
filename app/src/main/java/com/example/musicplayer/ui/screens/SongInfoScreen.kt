package com.example.musicplayer.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.media3.session.MediaController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import com.example.musicplayer.media.MediaManager
import com.example.musicplayer.ui.components.SongCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongInfoScreen(
    id: String?,
    title: String,
    artist: String?,
    img: String?,
    mediaController: MediaController,
    pagerState: PagerState
) {

    val mediaManager = MediaManager(mediaController)
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonSogInfo(onClick = { /*TODO*/ }, imageVector = Icons.Filled.Check)
                ButtonSogInfo(
                    onClick = {
                        if (!mediaManager.checkIsPlaying()){
                            mediaManager.startPlaying()
                        }
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    imageVector = Icons.Filled.PlayArrow
                )
                ButtonSogInfo(onClick = { /*TODO*/ }, imageVector = Icons.Filled.List)
            }

            SongCard(
                id = id,
                title = title,
                artist = artist,
                img = img,
                onClick = { /*TODO*/ },
                mediaController = mediaManager.getMediaController()
            )
        }
    }
}

@Composable
fun ButtonSogInfo(
    onClick: () -> Unit,
    imageVector: ImageVector
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(48, 79, 254)
        )
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "icon_for_buttonInfo",
            tint = Color.White
        )
    }
}