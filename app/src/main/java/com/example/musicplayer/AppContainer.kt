package com.example.musicplayer


import android.annotation.SuppressLint
import android.content.Context
import androidx.media3.exoplayer.offline.DownloadManager
import com.example.musicplayer.auth.AuthInterceptor
import com.example.musicplayer.auth.TokenAuthenticator
import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.data.NetworkMusicPlayerRepository
import com.example.musicplayer.data.network.MusicService
import com.example.musicplayer.datastore.MyDataStore
import com.example.musicplayer.download.DownloadManagerImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@SuppressLint("UnsafeOptInUsageError")
interface AppContainer {
    val musicPlayerRepository: MusicPlayerRepository
    val authInterceptor: AuthInterceptor
    val dataStore: MyDataStore
    val downloadManager: DownloadManager
    val downloadManagerImpl: DownloadManagerImpl
    val tokenAuthenticator: TokenAuthenticator
}

class DefaultAppContainer(context: Context) : AppContainer {

    private val BASE_URL = "http://45.15.158.128:8080"

    override val authInterceptor = AuthInterceptor()
    override val dataStore = MyDataStore(context)
    @SuppressLint("UnsafeOptInUsageError")
    override val downloadManager = DownloadManagerImpl(context).downloadManager
    override val downloadManagerImpl = DownloadManagerImpl(context)

    override val musicPlayerRepository: MusicPlayerRepository by lazy {
        NetworkMusicPlayerRepository(retrofitService)
    }

    override val tokenAuthenticator: TokenAuthenticator by lazy {
        TokenAuthenticator(musicPlayerRepository, authInterceptor, dataStore)
    }

    private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val httpClient: OkHttpClient.Builder by lazy {
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .authenticator(tokenAuthenticator)
            .addInterceptor(authInterceptor)
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .build()
    }

    private val retrofitService: MusicService by lazy {
        retrofit.create(MusicService::class.java)
    }

}