package com.example.musicplayer.ui.screens

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.auth.GoogleSignInHelper
import com.example.musicplayer.ui.components.Loading
import com.example.musicplayer.ui.components.auth.UserInfoScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException


@Composable
fun AuthInfo(
    navController: NavController
) {
    val context = LocalContext.current
    val googleSignInHelper = GoogleSignInHelper(context)

    val loadingState = remember { mutableStateOf(true) }

    val displayName = remember { mutableStateOf<String?>(null) }
    val email = remember { mutableStateOf<String?>(null) }
    val photoUrl = remember { mutableStateOf<String?>(null) }
    val idToken = remember { mutableStateOf<String?>(null) }

    val googleSignInLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)

                    Log.d(
                        "GoogleSignInActivity",
                        "IdToken:${account.idToken}"
                    )

                    Log.d(
                        "GoogleSignInActivity",
                        "serverAuthCode:${account.serverAuthCode}"
                    )

                    account.idToken?.let {
                        val appContainer =
                            (context.applicationContext as MusicApplication).container
                        appContainer.authInterceptor.setToken(it)
                    }

                    displayName.value = account.displayName
                    email.value = account.email
                    photoUrl.value = account.photoUrl?.toString()
                    idToken.value = account.idToken

                    loadingState.value = false

                } catch (e: ApiException) {
                    Log.w("GoogleSignInActivity", "signInResult:failed code=${e.statusCode}")
                }
            } else {
                Log.w(
                    "GoogleSignInActivity",
                    "signInResult:cancelled resultCode=${result.resultCode}"
                )
            }
        }

    LaunchedEffect(Unit) {
        googleSignInHelper.signIn(googleSignInLauncher)
    }

    if (loadingState.value) {
        Loading()
    } else {
        UserInfoScreen(
            idToken = idToken.value,
            displayName = displayName.value,
            email = email.value,
            navController = navController,
            photoUrl = photoUrl.value)
    }
}


