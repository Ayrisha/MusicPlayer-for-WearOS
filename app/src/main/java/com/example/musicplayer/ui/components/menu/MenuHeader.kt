package com.example.musicplayer.ui.components.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.Text

@Composable
fun MenuHeader() {
    ListHeader {
        Text(
            text = "MusicPlayer",
            textAlign = TextAlign.Center
        )
    }
}