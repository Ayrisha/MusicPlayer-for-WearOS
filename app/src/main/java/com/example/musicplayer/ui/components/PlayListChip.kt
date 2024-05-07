package com.example.musicplayer.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import com.example.musicplayer.R

@Composable
fun PlayListChip(
    text: String
) {
    Chip(
        modifier = Modifier.fillMaxWidth().height(35.dp),
        onClick = {},
        colors = ChipDefaults.imageBackgroundChipColors(
            backgroundImagePainter = painterResource(id = R.drawable.playlist_music_outline),
            backgroundImageScrimBrush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF1C1B1F),
                    Color.DarkGray,
                    Color(alpha = 0.1f, red = 0f, green = 0f, blue = 0f)
                ),
                start = Offset.Zero,
                end = Offset.Infinite
            )
        ),
        border = ChipDefaults.chipBorder(),
    ){
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
    }
}