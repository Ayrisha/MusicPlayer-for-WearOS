package com.example.musicplayer.auth

import android.util.Log
import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.datastore.MyDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val musicRepository: MusicPlayerRepository,
    private val authenticator: AuthInterceptor,
    private val dataStore: MyDataStore
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401 || response.code == 403) {

            val refreshToken = runBlocking {
                dataStore.refreshToken.firstOrNull()
            }

            val accessToken = runBlocking {
                dataStore.accessToken.firstOrNull()
            }

            Log.d("TokenAuthenticator", "Get refreshToken before update: $refreshToken")
            Log.d("TokenAuthenticator", "Get accessToken before update: $accessToken")

            if (refreshToken == null) {
                return null
            }

            authenticator.setToken(refreshToken)

            var newToken = ""

            runBlocking {
                try {
                    val tokens = musicRepository.auth()
                    Log.d("TokenAuthenticator", "Get refreshToken after update: ${tokens.refreshToken}")
                    Log.d("TokenAuthenticator", "Get accessToken after update: ${tokens.accessToken}")

                    newToken = tokens.accessToken

                    dataStore.updateAccessToken(tokens.accessToken)

                    withContext(Dispatchers.Main) {
                        authenticator.setToken(tokens.accessToken)

                        response.request.newBuilder()
                            .header("Authorization", "Bearer ${tokens.accessToken}")
                            .build()
                    }
                } catch (e: Exception) {
                    Log.e("TokenAuthenticator", "Error refreshing tokens: ${e.message}")
                }
            }
            return response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        }
        return null
    }
}