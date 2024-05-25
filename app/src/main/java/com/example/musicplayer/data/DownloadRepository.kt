package com.example.musicplayer.data

import android.content.Context
import com.example.musicplayer.data.model.Track

interface DownloadRepository {
    suspend fun getDownloadedTracks(): List<Track>
}