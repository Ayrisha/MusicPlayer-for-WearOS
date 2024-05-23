package com.example.musicplayer.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import com.example.musicplayer.ui.components.auth.CardSigIn

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AuthScreen(
    navController: NavController
) {
    val listState = rememberLazyListState()

    Scaffold(
        positionIndicator = {
            PositionIndicator(lazyListState = listState)
        }
    ) {
        LazyColumn(
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item {
                Text(
                    modifier = Modifier
                        .padding(top = 25.dp, start = 20.dp, end = 20.dp, bottom = 10.dp),
                    text = "Зарегестрируйтесь,\n чтобы иметь возможность сохранять любимые треки и создавать плейлисты",
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            item {
                CardSigIn(
                    imageVector = Icons.Rounded.AccountCircle,
                    text = "Войти через Google",
                    onClick = {
                        navController.navigate("AuthInfo")
                    },
                    backgroundPainter = ColorPainter(Color(48, 79, 254))
                )
            }
            item {
                CardSigIn(
                    imageVector = Icons.Rounded.Clear,
                    text = "Продолжить без аккаунта",
                    onClick = {
                        navController.navigate("menu")
                    },
                    backgroundPainter = ColorPainter(Color(0xFF1C1B1F)),
                    padding = 20.dp
                )
            }
        }
    }
}

