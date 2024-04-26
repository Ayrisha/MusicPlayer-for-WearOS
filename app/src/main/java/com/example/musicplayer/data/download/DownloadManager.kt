//package com.example.musicplayer.data.download
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.os.Environment
//import androidx.core.net.toUri
//import com.android.volley.Request
//
//@SuppressLint("UnsafeOptInUsageError")
//class DownloadManager(
//    private val context: Context,
//    private val title: String
//): Downloader {
//
//    private val downloadManager = context.getSystemService(DownloadManager::class.java)
//
//    override fun download(url: String): Long {
//        val request = DownloadManager.Request(url.toUri())
//            .setMimeType("image/jpeg")
//            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
//            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            .setTitle(title)
//            .addRequestHeader("Authorization", "Bearer <token>")
//            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "image.jpg")
//        return downloadManager.enqueue(request)
//    }
//}