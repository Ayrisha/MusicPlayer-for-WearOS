package com.example.musicplayer.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class Track (
    val id: String? = null,
    val title: String,
    val artist: String? = null,
    val imgLink: String?
)