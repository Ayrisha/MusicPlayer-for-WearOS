package com.example.musicplayer.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, name = "WEAR_OS_SMALL_ROUND")
@Preview(device = Devices.WEAR_OS_SQUARE, name = "WEAR_OS_SQUARE")
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, name = "WEAR_OS_LARGE_ROUND")
@Composable
fun PreviewLoading() {
    Loading()
}

@Composable
fun Loading(
) {
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoading.value = true
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading.value) {
            CircularProgressIndicator(
                color = Color(0xFF1C1B1F),
                trackColor = Color(0xff304ffe),
            )
        }
    }
}