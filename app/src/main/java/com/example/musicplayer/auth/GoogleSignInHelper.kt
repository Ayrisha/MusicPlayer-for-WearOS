package com.example.musicplayer.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.rememberCoroutineScope
import androidx.wear.activity.ConfirmationActivity
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.datastore.MyDataStore
import com.example.musicplayer.datastore.myDataStore
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import kotlinx.coroutines.runBlocking

private const val CLIENT_ID =
    "550470458277-2dvd7f06dd8npndqtc6o9kt9tcudjofn.apps.googleusercontent.com"

class GoogleSignInHelper(private val context: Context) {
    private val googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(CLIENT_ID)
            .requestServerAuthCode(CLIENT_ID)
            .requestScopes(Scope("openid"))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun signIn(googleSignInLauncher: ActivityResultLauncher<Intent>) {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    fun signOut() {
        val application = context.applicationContext as MusicApplication

        runBlocking {
            application.container.dataStore.clearTokens()
            application.container.authInterceptor.setToken("")
        }

        googleSignInClient.signOut()
    }

    fun isUserSignedIn(): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return account != null
    }

    fun getEmail(): String?{
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return account?.email
    }
}