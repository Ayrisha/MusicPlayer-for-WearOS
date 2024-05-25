package com.example.musicplayer.media

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
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
        mediaController.play()
    }

    fun sedMediaItem(track: Track) {
        if (mediaController.mediaItemCount != 0) {
            mediaController.clearMediaItems()
        }

        val mediaItem =
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

        mediaController.setMediaItem(mediaItem)
        mediaController.play()
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