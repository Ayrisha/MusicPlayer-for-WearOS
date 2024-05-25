package com.example.musicplayer


import android.annotation.SuppressLint
import android.content.Context
import androidx.media3.exoplayer.offline.DownloadManager
import com.example.musicplayer.auth.AuthInterceptor
import com.example.musicplayer.auth.TokenAuthenticator
import com.example.musicplayer.data.LocalDownloadRepository
import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.data.NetworkMusicPlayerRepository
import com.example.musicplayer.data.network.MusicService
import com.example.musicplayer.datastore.MyDataStore
import com.example.musicplayer.download.DownloadManagerImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@SuppressLint("UnsafeOptInUsageError")
interface AppContainer {
    val musicPlayerRepository: MusicPlayerRepository
    val localDownloadRepository: LocalDownloadRepository
    val authInterceptor: AuthInterceptor
    val dataStore: MyDataStore
    val downloadManagerImpl: DownloadManagerImpl
    val tokenAuthenticator: TokenAuthenticator
}

class DefaultAppContainer(context: Context) : AppContainer {

    private val BASE_URL = "http://45.15.158.128:8080"

    override val authInterceptor = AuthInterceptor()
    override val dataStore = MyDataStore(context)
    override val downloadManagerImpl = DownloadManagerImpl(context)

    private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val initialHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(initialHttpClient)
        .build()

    private val retrofitService: MusicService = retrofit.create(MusicService::class.java)

    override val musicPlayerRepository: MusicPlayerRepository = NetworkMusicPlayerRepository(retrofitService)

    override val localDownloadRepository: LocalDownloadRepository = LocalDownloadRepository(downloadManagerImpl)

    override val tokenAuthenticator: TokenAuthenticator = TokenAuthenticator(musicPlayerRepository, authInterceptor, dataStore)

    private val authenticatedHttpClient: OkHttpClient by lazy {
        initialHttpClient.newBuilder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }

    init {
        updateRetrofitClient()
    }

    private fun updateRetrofitClient() {
        val newRetrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(authenticatedHttpClient)
            .build()

        (musicPlayerRepository as NetworkMusicPlayerRepository).updateRetrofitService(newRetrofit.create(MusicService::class.java))
    }
}