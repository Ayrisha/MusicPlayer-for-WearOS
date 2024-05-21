package com.example.musicplayer.ui.components.like

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.Text

@Composable
fun LikeHeader() {
    ListHeader {
        Text(
            text = "Избранное",
            textAlign = TextAlign.Center
        )
    }
}