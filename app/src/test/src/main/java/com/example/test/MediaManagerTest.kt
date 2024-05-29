package com.example.test

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import com.example.musicplayer.data.model.Track
import com.example.musicplayer.media.MediaManager
import com.google.common.base.Verify.verify

class MediaManagerTest {
    private lateinit var mediaController: MediaController
    private lateinit var mediaManager: MediaManager

    @Test
    fun setMediaItem() {

    }

    @Test
    fun sedMediaItem() {
        val mediaItem = MediaItem.Builder()
            .setMediaId("1")
            .setUri("http://45.15.158.128:8080/hse/api/v1/music-player-dictionary/music/1.mp3")
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setArtist("Artist 1")
                    .setTitle("Song 1")
                    .build()
            )
            .build()

        mediaController.setMediaItem(mediaItem)

        assert(mediaController.mediaItemCount == 1)
    }

    @Test
    fun checkIsPlayingId() {
    }

    @Test
    fun checkIsPlaying() {
    }
}