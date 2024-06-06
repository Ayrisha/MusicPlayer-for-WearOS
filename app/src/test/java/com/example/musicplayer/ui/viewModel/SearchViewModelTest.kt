package com.example.musicplayer.ui.viewModel

import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.data.model.Track
import com.example.musicplayer.ui.viewModel.state.TrackUiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class SearchViewModelTest {

    lateinit var musicPlayerRepository: MusicPlayerRepository
    lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        musicPlayerRepository = mockk(relaxed = true)
        viewModel = SearchViewModel(musicPlayerRepository)
    }

    @Test
    fun `test popularTrack success`() {
        val mockPopularTracks = listOf(
            Track("1", "Song 1", "Artist 1", "cover1"),
            Track("2", "Song 2", "Artist 2", "cover2")
        )
        every { runBlocking { musicPlayerRepository.popularTrack() } } returns mockPopularTracks

        viewModel.popularTrack()

        assertThat(viewModel.trackUiState, `is`(TrackUiState.Start(mockPopularTracks)))
    }

    @Test
    fun `test popularTrack empty list`() {
        every { runBlocking { musicPlayerRepository.popularTrack() } } returns emptyList()

        viewModel.popularTrack()

        assertThat(viewModel.trackUiState, `is`(TrackUiState.Error))
    }

    @Test
    fun `test popularTrack network error`() {
        every { runBlocking { musicPlayerRepository.popularTrack() } } throws IOException()

        viewModel.popularTrack()

        assertThat(viewModel.trackUiState, `is`(TrackUiState.Error))
    }

    @Test
    fun `searchTrack should return Success when tracks are found`(){
        val title = "Test Track"
        val mockTrackList = listOf(
            Track("1", "Song 1", "Artist 1", "cover1"),
            Track("2", "Song 2", "Artist 2", "cover2")
        )

        every { runBlocking { musicPlayerRepository.searchTrack(title) } } returns mockTrackList

        viewModel.searchTrack(title)

        assert(viewModel.trackUiState is TrackUiState.Success)

        assert((viewModel.trackUiState as TrackUiState.Success).trackSearches == mockTrackList)
    }

    @Test
    fun `test searchTrack empty list`() {
        val title = "Test Track"
        every { runBlocking { musicPlayerRepository.searchTrack(title) } } returns emptyList()

        viewModel.searchTrack(title)

        assertThat(viewModel.trackUiState, `is`(TrackUiState.Empty))
    }

    @Test
    fun `test searchTrack network error`() {
        val title = "Test Track"
        every { runBlocking { musicPlayerRepository.searchTrack(title) } } throws IOException()

        viewModel.searchTrack(title)

        assertThat(viewModel.trackUiState, `is`(TrackUiState.Error))
    }

}