package com.example.musicplayer.auth

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthInterceptor() : Interceptor {
    private var token: String? = null

    fun setToken(token: String) {
        this.token = token
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val authValue = "Bearer $token"
        val request: Request = chain.request()
        val authenticatedRequest: Request = request.newBuilder()
            .header("Authorization", authValue).build()
        return chain.proceed(authenticatedRequest)
    }
}