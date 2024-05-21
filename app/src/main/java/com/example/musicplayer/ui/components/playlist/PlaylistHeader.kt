package com.example.musicplayer.ui.components.playlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.Text

@Composable
fun PlaylistHeader() {
    ListHeader {
        Text(
            text = "Плейлисты",
            textAlign = TextAlign.Center
        )
    }
}