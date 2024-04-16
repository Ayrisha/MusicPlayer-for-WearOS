package com.example.musicplayer.network;

import com.example.musicplayer.network.model.TrackInfo
import retrofit2.http.GET;
import retrofit2.http.Path
import retrofit2.http.Query;

interface MusicService {
    @GET("search")
    suspend fun trackSearch(
        @Query("trackTitle") trackTitle: String
    ):List<TrackInfo>
}
