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
import java.net.SocketTimeoutException

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

            var newToken: String? = null

            runBlocking {
                try {
                    val tokens = withContext(Dispatchers.IO) {
                        musicRepository.auth()
                    }
                    Log.d(
                        "TokenAuthenticator",
                        "Get refreshToken after update: ${tokens.refreshToken}"
                    )
                    Log.d(
                        "TokenAuthenticator",
                        "Get accessToken after update: ${tokens.accessToken}"
                    )

                    newToken = tokens.accessToken
                    dataStore.updateAccessToken(tokens.accessToken)
                    authenticator.setToken(tokens.accessToken)
                } catch (e: Exception) {
                    Log.e("TokenAuthenticator", "Error refreshing tokens: ${e.message}")
                    if (e is SocketTimeoutException) {
                        Log.e("TokenAuthenticator", "Timeout occurred while refreshing tokens")
                    }
                    return@runBlocking
                }
            }

            return newToken?.let {
                response.request.newBuilder()
                    .header("Authorization", "Bearer $it")
                    .build()
            }
        }
        return null
    }
}