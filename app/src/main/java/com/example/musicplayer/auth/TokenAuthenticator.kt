package com.example.musicplayer.auth

import android.util.Log
import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.datastore.MyDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val musicRepository: MusicPlayerRepository,
    private val authenticator: AuthInterceptor,
    private val dataStore: MyDataStore
) : Authenticator{
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401 || response.code == 403) {

            val refreshToken = runBlocking {
                dataStore.refreshToken.firstOrNull()
            }

            Log.d("TokenAuthenticator", "Get refreshToken from Datastore: $refreshToken")

            if (refreshToken != null) {
                authenticator.setToken(refreshToken)
            }

            val tokens = runBlocking { musicRepository.auth()}

            Log.d("accessToken", tokens.accessToken)
            Log.d("refreshToken", tokens.refreshToken)

            runBlocking{
                dataStore.updateRefreshToken(tokens.refreshToken)
                dataStore.updateAccessToken(tokens.accessToken)
            }

            authenticator.setToken(tokens.accessToken)

            return response.request.newBuilder()
                .header("Authorization", "Bearer ${tokens.accessToken}")
                .build()
        }
        return null
    }
}