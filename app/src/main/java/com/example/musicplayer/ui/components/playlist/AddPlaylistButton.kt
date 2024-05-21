package com.example.musicplayer.ui.components.playlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults

@Composable
fun AddPlaylistButton(
    navController: NavController
){
    Button(
        modifier = Modifier
            .size(ButtonDefaults.SmallButtonSize),
        onClick = {
            navController.navigate("addplaylist")
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(48, 79, 254)
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "item image",
                tint = Color.White
            )
        }
    }
}