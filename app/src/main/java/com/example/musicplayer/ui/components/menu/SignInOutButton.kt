package com.example.musicplayer.ui.components.menu

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.example.musicplayer.auth.GoogleSignInHelper
import kotlinx.coroutines.launch

@Composable
fun SignInOutButton(navController: NavController, isUserSignedIn: MutableState<Boolean>, googleSignInHelper: GoogleSignInHelper) {
    val coroutineScope = rememberCoroutineScope()
    Button(
        modifier = Modifier.fillMaxSize(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF1C1B1F),
            contentColor = Color.White
        ),
        onClick = {
            if (isUserSignedIn.value) {
                googleSignInHelper.signOut()
                isUserSignedIn.value = false
            } else {
                navController.navigate("auth")
            }
        }
    ) {
        Text(text = if (isUserSignedIn.value) "Выйти из аккаунта" else "Войти")
    }
}