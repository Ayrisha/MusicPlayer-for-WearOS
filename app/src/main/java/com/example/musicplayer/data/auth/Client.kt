package com.example.musicplayer.data.auth

import android.content.Context
import androidx.activity.ComponentActivity
import io.appwrite.Client
import io.appwrite.enums.OAuthProvider
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Account
import java.lang.Exception

private class ClientInit(context: Context) {
    var client = Client(context)
        .setEndpoint("https://cloud.appwrite.io/v1")
        .setProject("66225073bc16160995ea")
}

class Client {
    suspend fun loginWithGoogle(activity: ComponentActivity, context: Context): ResourceResponse {
        var account = Account(ClientInit(context).client)
        try {
            account.createOAuth2Session(activity = activity, provider = OAuthProvider.GOOGLE)
            var data = account.get()
            return ResourceResponse(name = data.name, email = data.email, error = false)
        } catch (e: AppwriteException) {
            return ResourceResponse(error = true)
        }
    }

    suspend fun logOut(context: Context) {
        var account = Account(ClientInit(context).client)
        try {
            account.deleteSessions()
        } catch (e: AppwriteException) {
            throw Exception("error login out")
        }
    }
}