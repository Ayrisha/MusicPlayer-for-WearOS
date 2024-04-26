package com.example.musicplayer.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Text

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LikeCard(
) {
    Card(
        backgroundPainter = ColorPainter(color = Color(0xFF1C1B1F)),
        modifier = Modifier
            .size(height = 60.dp, width = 170.dp),
        onClick = {  }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
//            AsyncImage(
//                model = img,
//                contentDescription = "Profile picture",
//                modifier = Modifier
//                    .size(30.dp)
//                    .clip(CircleShape),
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
                    text = "title",
                    color = Color.White,
                    fontSize=15.sp)
                Text(
                    text = "artist", Modifier.basicMarquee(),
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize=10.sp)
            }
        }
    }
}