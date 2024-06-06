package com.example.musicplayer.ui.components.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Card
import coil.decode.ImageSource
import com.example.musicplayer.R
import com.example.musicplayer.ui.components.add_playlist.AddTextField

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, name = "WEAR_OS_SMALL_ROUND")
@Preview(device = Devices.WEAR_OS_SQUARE, name = "WEAR_OS_SQUARE")
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, name = "WEAR_OS_LARGE_ROUND")
@Composable
fun PreviewCardSigIn() {
    CardSigIn(Icons.Rounded.AccountCircle, "Войти с помощью Google", {}, ColorPainter(Color.Gray))
}

@Composable
fun CardSigIn(
    imageVector: ImageVector,
    text: String,
    onClick: () -> Unit,
    backgroundPainter: ColorPainter,
    padding: Dp = 0.dp
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
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