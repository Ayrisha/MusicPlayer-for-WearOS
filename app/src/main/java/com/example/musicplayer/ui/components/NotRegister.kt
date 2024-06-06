package com.example.musicplayer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Card
import com.example.musicplayer.R

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, name = "WEAR_OS_SMALL_ROUND")
@Preview(device = Devices.WEAR_OS_SQUARE, name = "WEAR_OS_SQUARE")
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, name = "WEAR_OS_LARGE_ROUND")
@Composable
fun PreviewNotRegister() {
    NotRegister({})
}

@Composable
fun NotRegister(
    registerAction: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Зарегестрируйтесь, чтобы продолжить",
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.5f)
            )
            Card(
                onClick = registerAction,
                modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally, true),
                backgroundPainter = ColorPainter(Color(32,33,36))
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_person_24),
                        contentDescription = "icon",
                        tint = Color.White
                    )
                    Text(
                        text = "Войти",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}