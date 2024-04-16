package com.example.musicplayer.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text

@Composable
fun EmptyBox(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = "По вашему запросу ничего не найдено",
            modifier = Modifier.width(150.dp),
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}