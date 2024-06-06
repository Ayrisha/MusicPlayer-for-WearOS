package com.example.musicplayer.ui.viewModel


import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.data.model.Track
import com.example.musicplayer.download.DownloadManagerImpl
import com.example.musicplayer.ui.viewModel.state.DownloadTrackState
import com.example.musicplayer.ui.viewModel.state.LoadTrackState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class DownloadViewModelTest {
    lateinit var musicPlayerRepository: MusicPlayerRepository
    lateinit var viewModel: DownloadViewModel
    lateinit var downloadManagerImpl: DownloadManagerImpl

    @Before
    fun setup() {
        downloadManagerImpl = mockk<DownloadManagerImpl>()
        musicPlayerRepository = mockk(relaxed = true)
        viewModel = DownloadViewModel(musicPlayerRepository, downloadManagerImpl)
        val lambdaSlot = slot<(String) -> Unit>()
        every { downloadManagerImpl.downloadCompleteListener?.let { it(capture(lambdaSlot).toString()) } } answers {
            lambdaSlot.captured.invoke("someId")
        }
    }

    @Test
    fun `getDownloadedTracks should return Empty state when repository returns empty list`() = runTest {

        val expectedTracks = emptyList<Track>()
        coEvery { musicPlayerRepository.getDownloadedTracks() } returns expectedTracks

        viewModel.getDownloadedTracks()

        assertEquals(DownloadTrackState.Empty, viewModel.downloadTrackState)
    }

    @Test
    fun `getDownloadedTracks should return NotEmpty state when repository returns non-empty list`() = runTest {

        val expectedTracks = listOf(
            Track("1", "title1", "artist1", ""),
            Track("1", "title1", "artist1", "")
        )
        coEvery { musicPlayerRepository.getDownloadedTracks() } returns expectedTracks

        viewModel.getDownloadedTracks()

        assertEquals(DownloadTrackState.NotEmpty(trackList = expectedTracks), viewModel.downloadTrackState)
    }

    @Test
    fun `getDownloadedTracks should return Empty state when repository throws IOException`() = runTest {

        coEvery { musicPlayerRepository.getDownloadedTracks() } throws IOException()

        viewModel.getDownloadedTracks()

        assertEquals(DownloadTrackState.Empty, viewModel.downloadTrackState)
    }
}