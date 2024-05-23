package com.example.musicplayer.download

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import com.example.musicplayer.data.model.Track
import com.google.android.horologist.annotations.ExperimentalHorologistApi

@SuppressLint("UnsafeOptInUsageError")
@ExperimentalHorologistApi
class DownloadTracker(
    private val downloadManager: DownloadManager
) {
    init {
        downloadManager.addListener(DownloadManagerListener())
    }

    private inner class DownloadManagerListener : DownloadManager.Listener {
        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: Exception?
        ) {
            when (download.state) {
                Download.STATE_QUEUED -> Log.d("DownloadManagerListener", "Загрузка ожидает")
                Download.STATE_DOWNLOADING -> Log.d(
                    "DownloadManagerListener",
                    "Загрузка в процессе"
                )

                Download.STATE_STOPPED -> Log.d(
                    "DownloadManagerListener",
                    "Загрузка приостановлена"
                )

                Download.STATE_COMPLETED -> {
                    Log.d("DownloadManagerListener", "Загрузка завершена")
                }

                Download.STATE_FAILED -> Log.d("DownloadManagerListener", "Загрузка неудачна")
                Download.STATE_REMOVING -> {
                }

                Download.STATE_RESTARTING -> {
                }
            }
        }

        override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
            Log.d("DownloadManagerListener", "onDownloadRemoved")
        }
    }

    fun addDownload(
        url: String,
        id: String,
        title: String,
        artist: String,
        imgUrl: String,
        context: Context
    ) {
        Log.d("DownloadTracker", "addDownload")

        val separator = ":"

        val dataString = "$title$separator$artist"

        val dataBytes = dataString.encodeToByteArray()

        val downloadTrackRequest =
            DownloadRequest.Builder(/* id = */ id, /* uri = */ Uri.parse(url))
                .setData(dataBytes)
                .build()

        val downloadImgRequest =
            DownloadRequest.Builder(/* id = */ id + "img", /* uri = */ Uri.parse(imgUrl))
                .setData(dataBytes)
                .build()

        downloadManager.addDownload(downloadTrackRequest)
        downloadManager.addDownload(downloadImgRequest)

        DownloadService.sendAddDownload(
            context,
            MediaDownloadServiceImpl::class.java,
            downloadTrackRequest,
            /* foreground= */ false
        )
    }

    fun removeDownload(downloadId: String) {
        downloadManager.removeDownload(downloadId)
    }


    fun getDownloadedTracks(): List<Track> {
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

                        Log.d("DownloadTracker", "trackId: $trackId")
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
                        Log.d("DownloadTracker", "uri: $imgLink")
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

    fun isSongDownloaded(downloadManager: DownloadManager, songId: String): Boolean {
        val downloads = downloadManager.downloadIndex.getDownloads(Download.STATE_COMPLETED)

        if (downloads.moveToFirst()) {
            do {
                val download = downloads.download
                val mediaId = download.request.id
                if (mediaId == songId) {
                    downloads.close()
                    return true
                }
            } while (downloads.moveToNext())
        }

        downloads.close()
        return false
    }
}
