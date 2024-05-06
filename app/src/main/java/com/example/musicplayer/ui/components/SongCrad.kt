package com.example.musicplayer.ui.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import androidx.navigation.NavController
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Text
import coil.compose.AsyncImage
import com.example.musicplayer.R
import com.example.musicplayer.data.Track

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongCard(
    index: Int,
    mediaController: MediaController,
    list: List<Track>,
    navController: NavController,
    id: String?,
    title: String,
    artist: String?,
    img: String?
) {
    Card(
        backgroundPainter = ColorPainter(color = Color(0xFF1C1B1F)),
        modifier = Modifier
            .fillMaxSize(),
        onClick = {
            mediaController.setMediaItems(list.map { track ->
                MediaItem.Builder()
                    .setMediaId(track.id!!)
                    .setUri("http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/music/${track.id}.mp3")
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setArtist(track.artist)
                            .setTitle(track.title)
                            .build()
                    )
                    .build()
            }, index, 0)
            navController.navigate("play_screen/$id/$title/$artist")
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            AsyncImage(
                model = "http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/image/$img.png",
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                onError = { errorState ->
                    val throwable = errorState.result.throwable
                    Log.e("AsyncImage", "Error: $throwable")
                },
                fallback = painterResource(R.drawable.baseline_person_24)
            )
//            Icon(
//                imageVector = Icons.Rounded.AccountCircle,
//                contentDescription = "photo",
//                tint = Color.White
//            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize=15.sp)
                artist?.let { Text(
                    text = it, Modifier.basicMarquee(),
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize=10.sp) }
            }
        }
    }
}