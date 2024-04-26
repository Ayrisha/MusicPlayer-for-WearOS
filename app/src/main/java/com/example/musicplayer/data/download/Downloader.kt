package com.example.musicplayer.data.download

interface Downloader {
    fun download(url: String): Long
}