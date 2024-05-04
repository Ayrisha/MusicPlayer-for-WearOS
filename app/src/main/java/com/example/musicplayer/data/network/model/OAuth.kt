package com.example.musicplayer.data.network.model

import com.google.gson.annotations.SerializedName

data class OAuth(
    @SerializedName("JSESSIONID") var id: String,
    @SerializedName("state") var state: String
)
