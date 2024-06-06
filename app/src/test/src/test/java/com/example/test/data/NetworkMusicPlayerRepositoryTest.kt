package com.example.musicplayer.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.musicplayer.data.network.MusicService
import com.example.musicplayer.data.network.model.PlayListInfo
import com.example.musicplayer.data.network.model.TrackInfo
import com.example.musicplayer.download.DownloadManagerImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class NetworkMusicPlayerRepositoryTest {

    @RelaxedMockK
    lateinit var musicService: MusicService

    @RelaxedMockK
    lateinit var downloadManagerImpl: DownloadManagerImpl

    lateinit var repository: NetworkMusicPlayerRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = NetworkMusicPlayerRepository(musicService, downloadManagerImpl)
    }

    @Test
    fun `searchTrack should return list of tracks`() = runTest {
        val searchQuery = "song"
        val searchResults = listOf(
            TrackInfo("1", "Song 1", "cover1", "Artist 1"),
            TrackInfo("2", "Song 2", "cover2","Artist 2")
        )
        coEvery { musicService.trackSearch(searchQuery) } returns searchResults

        val result = repository.searchTrack(searchQuery)

        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("Song 1", result[0].title)
        assertEquals("Artist 1", result[0].artist)
        assertEquals("cover1", result[0].imgLink)
    }

    @Test
    fun `getTracksLike should return list of liked tracks`() = runTest {
        val likedTracks = listOf(
            TrackInfo("1", "Song 1", "Artist 1", "cover1"),
            TrackInfo("2", "Song 2", "Artist 2", "cover2")
        )
        coEvery { musicService.getTracksLike() } returns likedTracks

        val result = repository.getTracksLike()

        assertEquals(2, result.size)
    }

    @Test
    fun `getPlayList should return list of playlists`() = runTest {
        val playlists = listOf(
            PlayListInfo("1", "Playlist 1"),
            PlayListInfo("2", "Playlist 2")
        )
        coEvery { musicService.getPlayList() } returns playlists

        val result = repository.getPlayList()

        assertEquals(2, result.size)
    }

    @Test
    fun `getPlayListTracks should return list of tracks for a given playlist title`() = runTest {
        val title = "Playlist 1"
        val tracks = listOf(
            TrackInfo("1", "Song 1", "Artist 1", "cover1"),
            TrackInfo("2", "Song 2", "Artist 2", "cover2")
        )
        coEvery { musicService.getPlayListTracks(title) } returns tracks

        val result = repository.getPlayListTracks(title)

        assertEquals(2, result.size)
    }
}