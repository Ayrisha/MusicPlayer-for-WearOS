package com.example.musicplayer.ui.components.menu

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text

@Composable
fun UserEmail(email: String?, isUserSignedIn: MutableState<Boolean>) {
    if (email != null && isUserSignedIn.value) {
        Text(
            text = email,
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Center,
            color = Color.White.copy(alpha = 0.5f)
        )
    }
}