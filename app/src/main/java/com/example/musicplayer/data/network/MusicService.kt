package com.example.musicplayer.data.network;

import com.example.musicplayer.data.network.model.TrackInfo
import retrofit2.http.GET;
import retrofit2.http.Query;

interface MusicService {
    @GET("search")
    suspend fun trackSearch(
        @Query("trackTitle") trackTitle: String
    ):List<TrackInfo>

    @GET("music/popular")
    suspend fun popularTrack(
    ):List<TrackInfo>

    @GET("music/new")
    suspend fun newTrack(
    ):List<TrackInfo>
}
