package com.example.musicplayer.ui.components.search

import androidx.wear.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.ListHeader

@Composable
fun SearchHeader(header: String) {
    ListHeader {
        Text(
            text = header,
            textAlign = TextAlign.Center
        )
    }
}