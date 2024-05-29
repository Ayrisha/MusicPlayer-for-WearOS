package com.example.musicplayer.media

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.WearUnsuitableOutputPlaybackSuppressionResolverListener
import com.example.musicplayer.MusicApplication

class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null
    private lateinit var player: ExoPlayer

    @OptIn(UnstableApi::class) override fun onCreate() {
        super.onCreate()

        val downloadCache = (this.applicationContext as MusicApplication).container.downloadManagerImpl.getDownloadCache()

        val cacheDataSourceFactory: DataSource.Factory =
            CacheDataSource.Factory()
                .setCache(downloadCache)
                .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
                .setCacheWriteDataSinkFactory(null)

        player =
            ExoPlayer.Builder(this)
                //.setSuppressPlaybackOnUnsuitableOutput(true)
                .setMediaSourceFactory(
                    DefaultMediaSourceFactory(this).setDataSourceFactory(cacheDataSourceFactory)
                )
                .build()

        //player.addListener(WearUnsuitableOutputPlaybackSuppressionResolverListener(this))

        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        mediaSession?.player?.let { player ->
            if (!player.playWhenReady
                || player.mediaItemCount == 0
                || player.playbackState == ExoPlayer.STATE_ENDED) {
                stopSelf()
            }
        }
    }
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    override fun onDestroy() {
        super.onDestroy()
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
    }
}