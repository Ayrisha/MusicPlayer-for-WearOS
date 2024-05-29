package com.example.musicplayer.data

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import com.example.musicplayer.data.model.PlayList
import com.example.musicplayer.data.model.Tokens
import com.example.musicplayer.data.model.Track
import com.example.musicplayer.data.network.MusicService
import com.example.musicplayer.data.network.model.TokensInfo
import com.example.musicplayer.download.DownloadManagerImpl

class NetworkMusicPlayerRepository(
    private var musicService: MusicService,
    private val downloadManagerImpl: DownloadManagerImpl,
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

    override suspend fun auth(): Tokens = musicService.auth().let {
        Tokens(it.accessToken, it.refreshToken)
    }

    @OptIn(UnstableApi::class) override suspend fun getDownloadedTracks(): List<Track> {
        val downloadManager = downloadManagerImpl.getDownloadedManager()
        val downloads = downloadManager.downloadIndex.getDownloads(Download.STATE_COMPLETED)
        val listMedia = mutableListOf<Track>()
        val imageMap = mutableMapOf<String, String>()

        if (downloads.moveToFirst()) {
            do {
                val download = downloads.download
                val id = download.request.id

                if (download.state == Download.STATE_COMPLETED) {
                    if (id.endsWith("img")) {
                        val trackId = id.removeSuffix("img")
                        imageMap[trackId] = download.request.uri.toString()
                    }
                }
            } while (downloads.moveToNext())
        }

        if (downloads.moveToFirst()) {
            do {
                val download = downloads.download
                val dataString = download.request.data.decodeToString()
                val id = download.request.id

                if (download.state == Download.STATE_COMPLETED) {
                    if (!id.endsWith("img")) {
                        val (title, artist) = dataString.split(":")
                        val imgLink = imageMap[id] ?: ""
                        val track = Track(
                            id = id,
                            title = title,
                            artist = artist,
                            imgLink = imgLink
                        )
                        listMedia.add(track)
                    }
                }
            } while (downloads.moveToNext())
        }

        downloads.close()

        return listMedia
    }

    fun updateRetrofitService(newService: MusicService) {
        musicService = newService
    }
}
