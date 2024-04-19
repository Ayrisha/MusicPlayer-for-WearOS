package com.example.musicplayer.data.auth

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.wear.phone.interactions.authentication.CodeChallenge
import androidx.wear.phone.interactions.authentication.CodeVerifier
import androidx.wear.phone.interactions.authentication.OAuthRequest
import androidx.wear.phone.interactions.authentication.OAuthResponse
import androidx.wear.phone.interactions.authentication.RemoteAuthClient
import com.example.musicplayer.MusicApplication
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG = "AuthPKCEViewModel"
private const val CLIENT_ID = "550470458277-2dvd7f06dd8npndqtc6o9kt9tcudjofn.apps.googleusercontent.com"
private const val CLIENT_SECRET = "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"

class AuthPKCEViewModel(application: Application) : AndroidViewModel(application) {

    fun startAuthFlow(context: Context) {
        viewModelScope.launch {
            val codeVerifier = CodeVerifier()

            val uri = Uri.Builder()
                .encodedPath("https://accounts.google.com/o/oauth2/v2/auth")
                .appendQueryParameter("scope", "https://www.googleapis.com/auth/userinfo.profile")
                .build()

            val oauthRequest = OAuthRequest.Builder(context)
                .setAuthProviderUrl(uri)
                .setCodeChallenge(CodeChallenge(codeVerifier))
                .setClientId(CLIENT_ID)
                .build()

            val code = retrieveOAuthCode(oauthRequest, context).getOrElse {
                return@launch
            }
        }
    }

    private suspend fun retrieveOAuthCode(
        oauthRequest: OAuthRequest,
        context: Context
    ): Result<String> {
        Log.d(TAG, "Authorization requested. Request URL: ${oauthRequest.requestUrl}")

        return suspendCoroutine { c ->
            RemoteAuthClient.create(context).sendAuthorizationRequest(
                request = oauthRequest,
                executor = { command -> command?.run() },
                clientCallback = object : RemoteAuthClient.Callback() {
                    override fun onAuthorizationError(request: OAuthRequest, errorCode: Int) {
                        Log.w(TAG, "Authorization failed with errorCode $errorCode")
                        c.resume(Result.failure(IOException("Authorization failed")))
                    }

                    override fun onAuthorizationResponse(
                        request: OAuthRequest,
                        response: OAuthResponse
                    ) {
                        val responseUrl = response.responseUrl
                        Log.d(TAG, "Authorization success. ResponseUrl: $responseUrl")
                        val code = responseUrl?.getQueryParameter("code")
                        if (code.isNullOrBlank()) {
                            Log.w(
                                TAG,
                                "Google OAuth 2.0 API token exchange failed. " +
                                        "No code query parameter in response URL."
                            )
                            c.resume(Result.failure(IOException("Authorization failed")))
                        } else {
                            Log.w(TAG, "Code retrieve")
                            c.resume(Result.success(code))
                        }
                    }
                }
            )
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MusicApplication)
                AuthPKCEViewModel(application)
            }
        }
    }
}
