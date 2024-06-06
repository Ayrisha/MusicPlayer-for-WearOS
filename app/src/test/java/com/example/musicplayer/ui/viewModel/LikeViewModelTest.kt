package com.example.musicplayer.ui.viewModel

import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.data.model.Track
import com.example.musicplayer.ui.viewModel.state.LikeState
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
import java.net.ConnectException

@RunWith(RobolectricTestRunner::class)
class LikeViewModelTest {
    lateinit var musicPlayerRepository: MusicPlayerRepository
    lateinit var viewModel: LikeViewModel

    @Before
    fun setup() {
        musicPlayerRepository = mockk(relaxed = true)
        viewModel = LikeViewModel(musicPlayerRepository)
    }

    @Test
    fun `getTracksLike should return Success state when repository returns non-empty list`() = runTest {

        val expectedTracks = listOf(
            Track("1", "title1", "artist1", ""),
            Track("1", "title1", "artist1", "")
        )
        coEvery { musicPlayerRepository.getTracksLike() } returns expectedTracks

        viewModel.getTracksLike()

        assertEquals(TrackListState.Success(expectedTracks.asReversed()), viewModel.likeUiState)
    }

    @Test
    fun `getTracksLike should return Empty state when repository returns empty list`() = runTest {

        val expectedTracks = emptyList<Track>()
        coEvery { musicPlayerRepository.getTracksLike() } returns expectedTracks

        // When
        viewModel.getTracksLike()

        // Then
        assertEquals(TrackListState.Empty, viewModel.likeUiState)
    }

    @Test
    fun `getTracksLike should return Error state when repository throws IOException`() = runTest {

        coEvery { musicPlayerRepository.getTracksLike() } throws IOException()

        viewModel.getTracksLike()

        assertEquals(TrackListState.Error, viewModel.likeUiState)
    }

    @Test
    fun `checkLikeTrack should set likeState to Like when checkLikeResponse returns 1`() = runTest {

        val idMedia = "TrackId"

        viewModel.checkLikeTrack(idMedia)

        assertEquals(LikeState.Like, viewModel.likeState)
    }

    @Test
    fun `checkLikeTrack should set likeState to Dislike when checkLikeResponse returns 0`() = runTest {

        val idMedia = "TrackId"
        coEvery { musicPlayerRepository.checkTrackLike(idMedia) } throws IOException()


        viewModel.checkLikeTrack(idMedia)

        assertEquals(LikeState.Dislike, viewModel.likeState)
    }

    @Test
    fun `checkLikeTrack should set likeState to NotConnection when checkLikeResponse throws ConnectException`() = runTest {

        val idMedia = "TrackId"
        coEvery { musicPlayerRepository.checkTrackLike(idMedia) } throws ConnectException()

        viewModel.checkLikeTrack(idMedia)

        assertEquals(LikeState.NotConnection, viewModel.likeState)
    }
}