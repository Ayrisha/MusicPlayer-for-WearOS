package com.example.musicplayer.data.network.model

import com.google.gson.annotations.SerializedName

data class TrackInfo (
    @SerializedName("trackId") var id: String? = null,
    @SerializedName("trackTitle") var trackTitle: String,
    @SerializedName("trackCoverId") var trackCoverId: String,
    @SerializedName("trackAuthor") var artist: String? = null
)
