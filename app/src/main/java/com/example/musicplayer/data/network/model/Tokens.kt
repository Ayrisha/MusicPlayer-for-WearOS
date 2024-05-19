package com.example.musicplayer.data.network.model

import com.google.gson.annotations.SerializedName

data class Tokens(
    @SerializedName("accessToken") var accessToken: String,
    @SerializedName("refreshToken") var refreshToken: String
)
