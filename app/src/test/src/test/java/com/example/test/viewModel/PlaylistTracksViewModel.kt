package com.example.musicplayer.ui.viewModel

import androidx.lifecycle.Observer
import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.data.model.Track
import com.example.musicplayer.ui.viewModel.state.TrackListState
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class PlaylistTracksViewModelTest {
    lateinit var musicPlayerRepository: MusicPlayerRepository
    lateinit var viewModel: PlaylistTracksViewModel

    @Before
    fun setup() {
        musicPlayerRepository = mockk(relaxed = true)
        viewModel = PlaylistTracksViewModel(musicPlayerRepository)
    }

    @Test
    fun `getTracks should return Success state when repository returns non-empty list`() = runTest {
        val expectedTracks = listOf(Track("1", "title1", "artist1", ""),
                                    Track("1", "title1", "artist1", ""))
        coEvery { musicPlayerRepository.getPlayListTracks(any()) } returns expectedTracks

        viewModel.getTracks("PlaylistTitle")

        assertEquals(TrackListState.Success(expectedTracks), viewModel.likeUiState)
    }

    @Test
    fun `getTracks should return Empty state when repository returns empty list`() = runTest {

        val expectedTracks = emptyList<Track>()
        coEvery { musicPlayerRepository.getPlayListTracks(any()) } returns expectedTracks

        viewModel.getTracks("PlaylistTitle")

        assertEquals(TrackListState.Empty, viewModel.likeUiState)
    }

    @Test
    fun `getTracks should return Error state when repository throws IOException`() = runTest {

        coEvery { musicPlayerRepository.getPlayListTracks(any()) } throws IOException()

        viewModel.getTracks("PlaylistTitle")

        assertEquals(TrackListState.Error, viewModel.likeUiState)
    }

    @Test
    fun `setTrack should set addTrackResult to success when repository operation succeeds`() = runTest {

        val title = "PlaylistTitle"
        val trackId = "TrackId"

        viewModel.setTrack(title, trackId)

        val expectedResult = Result.success("ok")
        val observer = Observer<Result<String>> {}
        viewModel.addTrackResult.observeForever(observer)
        assertEquals(expectedResult, viewModel.addTrackResult.value)
    }

    @Test
    fun `setTrack should set addTrackResult to failure when repository operation fails`() = runTest {

        val title = "PlaylistTitle"
        val trackId = "TrackId"
        val exception = Exception("Failed to set track")

        coEvery { musicPlayerRepository.setPlayListTrack(title, trackId) } throws exception
        viewModel.setTrack(title, trackId)

        val expectedFailure = Result.failure<String>(exception)
        val observer = Observer<Result<String>> {}
        viewModel.addTrackResult.observeForever(observer)
        assertEquals(expectedFailure, viewModel.addTrackResult.value)
    }
}