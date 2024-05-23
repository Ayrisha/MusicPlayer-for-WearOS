package com.example.musicplayer.media

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.session.MediaController
import com.example.musicplayer.data.model.Track

class MediaManager(private val mediaController: MediaController) {

    fun setMediaItems(list: List<Track>, index: Int) {
        if (mediaController.mediaItemCount != 0) {
            mediaController.clearMediaItems()
        }

        val mediaItems = list.map { track ->
            Log.d("MediaManager", "$track")
            MediaItem.Builder()
                .setMediaId(track.id.toString())
                .setUri("http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/music/${track.id}.mp3")
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setArtist(track.artist)
                        .setTitle(track.title)
                        .build()
                )
                .build()
        }
        mediaController.setMediaItems(mediaItems, index, 0)
    }

    fun addMediaItem(track: Track) {
        val mediaItem =
            track.id?.let {
                MediaItem.Builder()
                    .setMediaId(it)
                    .setUri("http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/music/${track.id}.mp3")
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setArtist(track.artist)
                            .setTitle(track.title)
                            .build()
                    )
                    .build()
            }
        if (mediaItem != null) {
            mediaController.addMediaItem(mediaItem)
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun addLoadMediaItem(){

    }

    fun checkIsPlayingId(id: String): Boolean{
        if (mediaController.isPlaying){
            return id == mediaController.currentMediaItem?.mediaId
        }
        return false
    }

    fun checkIsPlaying(): Boolean{
        return mediaController.isPlaying
    }

    fun startPlaying(){
        mediaController.play()
    }

    fun getMediaController(): MediaController{
        return mediaController
    }
}