package com.example.musicplayer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItem(
    contents: String,
    painterId: Int,
    navController: NavController,
    screen: String
) {
    ElevatedCard(
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1C1B1F)
        ),
        modifier = Modifier
            .size(width = 80.dp, height = 80.dp),
        onClick = { navController.navigate(screen) }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Image(
                    painter = painterResource(id = painterId),
                    contentDescription = "item image",
                    modifier = Modifier.size(25.dp)
                )
                Text(text = contents)
            }
        }
    }
}