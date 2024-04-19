package com.example.musicplayer.data

import com.example.musicplayer.data.network.MusicService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer{
    val musicPlayerRepository: MusicPlayerRepository
}

class DefaultAppContainer : AppContainer{
    private val BASE_URL = "http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/"

    private val logging : HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val httpClient : OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor(logging)

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .build()

    private val retrofitService: MusicService by lazy {
        retrofit.create(MusicService::class.java)
    }

    override val musicPlayerRepository: MusicPlayerRepository by lazy {
        NetworkMusicPlayerRepository(retrofitService)
    }
}