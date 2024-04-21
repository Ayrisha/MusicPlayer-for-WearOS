package com.example.musicplayer.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import androidx.wear.compose.material.Card

@Composable
fun MinimalDialog(
    onDismissRequest: () -> Unit,
    text: String?
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(1f)
        Card(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
            shape = RoundedCornerShape(16.dp),
            backgroundPainter = ColorPainter(Color(0xFF1C1B1F)),
            onClick = {}
        ) {
            if (text != null) {
                Text(
                    text = text,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontSize = 10.sp,
                    color = Color.White
                )
            }
        }
    }
}