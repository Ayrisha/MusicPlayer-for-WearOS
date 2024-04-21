package com.example.musicplayer.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Text

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SongCard(
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
        onClick = { navController.navigate("play_screen/$id") }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
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