package com.example.musicplayer.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text

@Composable
fun Retry (
    retryAction: () -> Unit
){
    Column (
        modifier = Modifier.fillMaxSize().padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        IconButton(
            onClick = retryAction,
        ) {
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = "refresh",
                tint = Color(0xff304ffe)
            )
        }
        Text(text = "Попробуйте еще раз")
    }
}