package com.example.musicplayer.data

import com.example.musicplayer.data.model.PlayList
import com.example.musicplayer.data.model.Track
import com.example.musicplayer.data.network.model.Tokens

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
    suspend fun setPlayList(title: String)
    suspend fun getPlayList(): List<PlayList>
    suspend fun getPlayListTracks(title: String): List<Track>
    suspend fun setPlayListTrack(title: String, trackId: String)
    suspend fun deletePlayList(title: String)
    suspend fun deletePlayListTrack(title: String, trackId: String)
    suspend fun auth(): Tokens
}