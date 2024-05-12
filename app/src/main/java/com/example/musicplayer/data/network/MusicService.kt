package com.example.musicplayer.data.network;

import com.example.musicplayer.data.model.Track
import com.example.musicplayer.data.network.model.PlayListInfo
import com.example.musicplayer.data.network.model.TrackInfo
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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

    @GET("/oauth2/authorization/google")
    suspend fun oauth(
        @Header("User") user: String
    )

    @POST("/hse/api/v1/music-player-dictionary/likes/like")
    suspend fun setTrackLike(
        @Query("trackId") trackId: String
    )

    @DELETE("/hse/api/v1/music-player-dictionary/likes/like")
    suspend fun deleteTrackLike(
        @Query("trackId") trackId: String
    )

    @GET("/hse/api/v1/music-player-dictionary/likes/check")
    suspend fun checkTrackLike(
        @Query("trackId") trackId: String
    )

    @GET("/hse/api/v1/music-player-dictionary/likes")
    suspend fun getTracksLike(
    ):List<TrackInfo>


    @POST("/hse/api/v1/music-player-dictionary/playlists/playlist")
    suspend fun setPlayList(
        @Query("name") title: String
    )

    @DELETE("/hse/api/v1/music-player-dictionary/playlists/playlist")
    suspend fun deletePlayList(
        @Query("name") title: String
    )

    @GET("/hse/api/v1/music-player-dictionary/playlists")
    suspend fun getPlayList(
    ):List<PlayListInfo>

    @GET("/hse/api/v1/music-player-dictionary/playlists/tracks")
    suspend fun getPlayListTracks(
        @Query("name") title: String
    ):List<TrackInfo>

    @POST("/hse/api/v1/music-player-dictionary/playlists/track")
    suspend fun setPlayListTrack(
        @Query("name") title: String,
        @Query("trackId") trackId: String
    )

    @DELETE("/hse/api/v1/music-player-dictionary/playlists/track")
    suspend fun deletePlayListTrack(
        @Query("name") title: String,
        @Query("trackId") trackId: String
    )
}
