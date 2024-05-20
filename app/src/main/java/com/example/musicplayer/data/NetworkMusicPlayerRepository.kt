package com.example.musicplayer.data

import com.example.musicplayer.data.model.PlayList
import com.example.musicplayer.data.model.Track
import com.example.musicplayer.data.network.MusicService
import com.example.musicplayer.data.network.model.Tokens

class NetworkMusicPlayerRepository(
    private var musicService: MusicService
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

    override suspend fun setPlayList(title: String) {
        musicService.setPlayList(title)
    }

    override suspend fun getPlayList(): List<PlayList> = musicService.getPlayList().map { items ->
        PlayList(
            title = items.trackTitle
        )
    }

    override suspend fun getPlayListTracks(title: String): List<Track> = musicService.getPlayListTracks(title).map { items ->
        Track(
            id = items.id,
            title = items.trackTitle,
            artist = items.artist,
            imgLink = items.trackCoverId
        )
    }

    override suspend fun setPlayListTrack(title: String, trackId: String) {
        musicService.setPlayListTrack(title, trackId)
    }

    override suspend fun deletePlayList(title: String) {
        musicService.deletePlayList(title)
    }

    override suspend fun deletePlayListTrack(title: String, trackId: String) {
        musicService.deletePlayListTrack(title, trackId)
    }

    override suspend fun auth(): Tokens = musicService.auth()

    fun updateRetrofitService(newService: MusicService) {
        musicService = newService
    }
}
