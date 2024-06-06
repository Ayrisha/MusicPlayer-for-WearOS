package com.example.musicplayer.ui.components.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import androidx.wear.protolayout.ModifiersBuilders.Background
import com.example.musicplayer.R
import com.example.musicplayer.ui.components.auth.UserInfoScreen

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, name = "WEAR_OS_SMALL_ROUND")
@Preview(device = Devices.WEAR_OS_SQUARE, name = "WEAR_OS_SQUARE")
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, name = "WEAR_OS_LARGE_ROUND")
@Composable
fun PreviewUserInfoScreen() {
    MenuItem(painterResource(R.drawable.menu), "text",  rememberNavController(), " ")
}

@Composable
fun MenuItem(
    background: Painter,
    contents: String,
    navController: NavController,
    screen: String
) {
    Chip(
        modifier = Modifier
            .fillMaxWidth()
            .height(ChipDefaults.Height),
        onClick = {
            navController.navigate(screen)
        },
        colors = ChipDefaults.imageBackgroundChipColors(
            backgroundImagePainter = background,
            backgroundImageScrimBrush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF1C1B1F),
                    Color(0xFF1C1B1F),
                    Color(alpha = 0.1f, red = 0f, green = 0f, blue = 0f),
                    Color(alpha = 0.1f, red = 0f, green = 0f, blue = 0f)
                ),
                startX = 0.0f,
                endX = Float.POSITIVE_INFINITY
            )
        ),
        border = ChipDefaults.chipBorder(),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = contents,
                color = Color.White,
                fontSize = 15.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "item image",
                modifier = Modifier.size(25.dp),
                tint = Color.White
            )
        }
    }
}