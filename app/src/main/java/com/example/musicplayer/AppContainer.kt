package com.example.musicplayer


import android.annotation.SuppressLint
import android.content.Context
import androidx.media3.exoplayer.offline.DownloadManager
import com.example.musicplayer.auth.AuthInterceptor
import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.data.NetworkMusicPlayerRepository
import com.example.musicplayer.data.network.MusicService
import com.example.musicplayer.datastore.DataStoreManager
import com.example.musicplayer.datastore.MyDataStore
import com.example.musicplayer.download.DownloadManagerImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@SuppressLint("UnsafeOptInUsageError")
interface AppContainer {
    val musicPlayerRepository: MusicPlayerRepository
    val basicAuthInterceptor: AuthInterceptor
    val dataStore: MyDataStore
    val downloadManager: DownloadManager
}

class DefaultAppContainer(context: Context) : AppContainer {

    private val BASE_URL = "http://45.15.158.128:8080"

    override val basicAuthInterceptor = AuthInterceptor()
    override val dataStore = DataStoreManager.getInstance(context)
    @SuppressLint("UnsafeOptInUsageError")
    override val downloadManager = DownloadManagerImpl(context).downloadManager

    private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(basicAuthInterceptor)

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