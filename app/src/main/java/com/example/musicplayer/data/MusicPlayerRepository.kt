package com.example.musicplayer.data

import com.example.musicplayer.data.network.MusicService
import com.example.musicplayer.data.network.model.OAuth

interface MusicPlayerRepository {
    suspend fun searchTrack(title: String): List<Track>
    suspend fun popularTrack(): List<Track>
    suspend fun newTrack(): List<Track>
    suspend fun sedCode(code: String)
    suspend fun oauth(user:String)
    suspend fun getTracksLike(): List<Track>
    suspend fun setTrackLike(trackId: String)
    suspend fun deleteTrackLike(trackId: String)

    suspend fun checkTrackLike(trackId: String)
}

class NetworkMusicPlayerRepository(
    private val musicService: MusicService
): MusicPlayerRepository{
    override suspend fun searchTrack(
        title: String
    ): List<Track> = musicService.trackSearch(title).map { items ->
        Track(
            id = items.id,
            title = items.trackTitle,
            artist = items.artist,
            imgLink = items.trackCoverId
        )
    }
    override suspend fun popularTrack(
    ): List<Track> = musicService.popularTrack().map { items ->
        Track(
            id = items.id,
            title = items.trackTitle,
            artist = items.artist,
            imgLink = items.trackCoverId
        )
    }
    override suspend fun newTrack(
    ): List<Track> = musicService.newTrack().map { items ->
        Track(
            id = items.id,
            title = items.trackTitle,
            artist = items.artist,
            imgLink = items.trackCoverId
        )
    }

    override suspend fun sedCode(code: String) {
        musicService.sendCode(code)
    }

    override suspend fun oauth(user: String) {
        musicService.oauth(user)
    }

    override suspend fun getTracksLike(): List<Track> = musicService.getTracksLike().map { items ->
        Track(
            id = items.id,
            title = items.trackTitle,
            artist = items.artist,
            imgLink = items.trackCoverId
        )
    }

    override suspend fun setTrackLike(trackId: String) {
        musicService.setTrackLike(trackId)
    }

    override suspend fun deleteTrackLike(trackId: String) {
        musicService.deleteTrackLike(trackId)
    }

    override suspend fun checkTrackLike(trackId: String) {
        musicService.checkTrackLike(trackId)
    }
}
