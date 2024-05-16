package com.example.musicplayer.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.rememberCoroutineScope
import androidx.wear.activity.ConfirmationActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope

private const val CLIENT_ID =
    "550470458277-2dvd7f06dd8npndqtc6o9kt9tcudjofn.apps.googleusercontent.com"

class GoogleSignInHelper(private val context: Context) {
    private val googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(CLIENT_ID)
            .requestServerAuthCode(CLIENT_ID)
            .requestScopes(Scope("openid"), Scope("email"), Scope("profile"))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun signIn(googleSignInLauncher: ActivityResultLauncher<Intent>) {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }
}