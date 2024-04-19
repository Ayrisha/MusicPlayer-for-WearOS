package com.example.musicplayer.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

private const val CLIENT_ID =
    "550470458277-hgcg4jt8lstgt9g7kc7j1b6iiavsur77.apps.googleusercontent.com"

@Composable
fun AuthScreen(
) {
    val googleSignInLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d("GoogleSignInActivity", "Received Activity Result: ${result.resultCode}")
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("GoogleSignInActivity", "signInResult:success account=${account?.displayName}")
            } catch (e: ApiException) {
                Log.w("GoogleSignInActivity", "signInResult:failed code=${e.statusCode}")
            }
        } else {
            Log.w("GoogleSignInActivity", "signInResult:cancelled resultCode=${result.resultCode}")
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 4.dp)
    ) {
       GoogleSignInScreen(googleSignInLauncher = googleSignInLauncher)
    }
}

@Composable
fun GoogleSignInScreen(googleSignInLauncher: ActivityResultLauncher<Intent>) {
    val context = LocalContext.current
    val isSignedIn by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isSignedIn) {
            androidx.compose.material3.Text(text = "Вы успешно авторизованы!")
        } else {
            Button(
                onClick = { signInWithGoogle(googleSignInLauncher, context) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(48,79,254)
                )
            ) {
                androidx.compose.material3.Text(text = "Войти через Google")
            }
        }
    }
}

fun signInWithGoogle(googleSignInLauncher: ActivityResultLauncher<Intent>, context: Context) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .build()
    val signInIntent = GoogleSignIn.getClient(context, gso).signInIntent
    googleSignInLauncher.launch(signInIntent)
}
