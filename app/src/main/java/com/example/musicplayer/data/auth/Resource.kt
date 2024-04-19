package com.example.musicplayer.data.auth

data class ResourceResponse(
    var name: String? = null,
    var email: String? = null,
    var error: Boolean? = null,
)