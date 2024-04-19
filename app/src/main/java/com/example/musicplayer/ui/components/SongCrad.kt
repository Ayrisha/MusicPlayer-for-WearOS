package com.example.musicplayer.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Text
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.musicplayer.R

@Composable
fun SongCard(
    navController: NavController,
    id: String?,
    title: String,
    artist: String?,
    img: String?
) {
    Card(
        onClick = { navController.navigate("play_screen/$id") },
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row{
//            AsyncImage(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .clip(RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp)),
//                model = ImageRequest.Builder(context = LocalContext.current)
//                    .data(img?.replace("http", "https"))
//                    .crossfade(true)
//                    .build(),
//                error = painterResource(id = R.drawable.default_img),
//                placeholder = painterResource(id = R.drawable.default_img),
//                contentDescription = "img",
//                contentScale = ContentScale.Crop
//            )
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = "photo",
                tint = Color.White
            )
            Column {
                Text(text = title)
                artist?.let { Text(text = it) }
            }
        }
    }
}