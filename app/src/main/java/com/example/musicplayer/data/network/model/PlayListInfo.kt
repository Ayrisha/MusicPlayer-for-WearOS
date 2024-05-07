package com.example.musicplayer.data.network.model

import com.google.gson.annotations.SerializedName

data class PlayListInfo (
    @SerializedName("tickets") var tickets: String,
    @SerializedName("playlistName") var trackTitle: String
)