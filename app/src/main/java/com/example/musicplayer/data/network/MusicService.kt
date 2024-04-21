package com.example.musicplayer.data.network;

import com.example.musicplayer.data.network.model.TrackInfo
import retrofit2.http.GET
import retrofit2.http.Query


interface MusicService {
    @GET("/hse/api/v1/music-player-dictionary/search")
    suspend fun trackSearch(
        @Query("trackTitle") trackTitle: String
    ):List<TrackInfo>

    @GET("/hse/api/v1/music-player-dictionary/music/popular")
    suspend fun popularTrack(
    ):List<TrackInfo>

    @GET("/hse/api/v1/music-player-dictionary/music/new")
    suspend fun newTrack(
    ):List<TrackInfo>

    @GET("/login/oauth2/code/google")
    suspend fun sendCode(
        @Query("code") code: String
    )
}
