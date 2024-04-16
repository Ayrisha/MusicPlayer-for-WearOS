package com.example.musicplayer.data

import com.example.musicplayer.network.MusicService

interface MusicPlayerRepository {
    suspend fun searchTrack(title: String): List<Track>
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
}
