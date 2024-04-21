package com.example.musicplayer.data

import com.example.musicplayer.data.network.MusicService

interface MusicPlayerRepository {
    suspend fun searchTrack(title: String): List<Track>
    suspend fun popularTrack(): List<Track>
    suspend fun newTrack(): List<Track>
    suspend fun sedCode(code: String)
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
}
