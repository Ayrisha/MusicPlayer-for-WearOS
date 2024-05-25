package com.example.musicplayer.data

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import com.example.musicplayer.data.model.Track
import com.example.musicplayer.download.DownloadManagerImpl
import com.example.musicplayer.download.MediaDownloadServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class LocalDownloadRepository(
    private val downloadManagerImpl: DownloadManagerImpl,
) : DownloadRepository{
    @OptIn(UnstableApi::class) override suspend fun getDownloadedTracks(): List<Track> {
        val downloadManager = downloadManagerImpl.getDownloadedManager()
        val downloads = downloadManager.downloadIndex.getDownloads(Download.STATE_COMPLETED)
        val listMedia = mutableListOf<Track>()
        val imageMap = mutableMapOf<String, String>()

        if (downloads.moveToFirst()) {
            do {
                val download = downloads.download
                val id = download.request.id

                if (download.state == Download.STATE_COMPLETED) {
                    if (id.endsWith("img")) {
                        val trackId = id.removeSuffix("img")
                        imageMap[trackId] = download.request.uri.toString()
                    }
                }
            } while (downloads.moveToNext())
        }

        if (downloads.moveToFirst()) {
            do {
                val download = downloads.download
                val dataString = download.request.data.decodeToString()
                val id = download.request.id

                if (download.state == Download.STATE_COMPLETED) {
                    if (!id.endsWith("img")) {
                        val (title, artist) = dataString.split(":")
                        val imgLink = imageMap[id] ?: ""
                        val track = Track(
                            id = id,
                            title = title,
                            artist = artist,
                            imgLink = imgLink
                        )
                        listMedia.add(track)
                    }
                }
            } while (downloads.moveToNext())
        }

        downloads.close()

        return listMedia
    }
}