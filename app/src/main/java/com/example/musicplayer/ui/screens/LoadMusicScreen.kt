package com.example.musicplayer.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults

@Composable
fun LoadMusicScreen() {
    val listState = rememberScalingLazyListState()
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState,
        flingBehavior = ScalingLazyColumnDefaults.snapFlingBehavior(
            state = listState
        )
    ) {
        items(5) {
            Chip(
                modifier = Modifier
                    .fillMaxWidth().height(ChipDefaults.Height),
                onClick = {
                },
                colors = ChipDefaults.chipColors(
                    backgroundColor = Color(0xFF1C1B1F),
                ),
                border = ChipDefaults.chipBorder(),
                contentPadding = PaddingValues(10.dp)
            ){}
        }
    }
}