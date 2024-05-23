package com.example.musicplayer.ui.components.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Card

@Composable
fun CardSigIn(
    imageVector: ImageVector,
    text: String,
    onClick: () -> Unit,
    backgroundPainter: ColorPainter,
    padding: Dp = 0.dp) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, bottom = padding),
        onClick = onClick,
        backgroundPainter = backgroundPainter,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = "icon",
                tint = Color.White
            )
            Text(
                text = text,
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}