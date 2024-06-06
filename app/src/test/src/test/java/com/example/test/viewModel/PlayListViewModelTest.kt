package com.example.musicplayer.ui.viewModel

import com.example.musicplayer.data.MusicPlayerRepository
import com.example.musicplayer.data.model.PlayList
import com.example.musicplayer.ui.viewModel.state.PlayListUiState
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
class PlayListViewModelTest {
    lateinit var musicPlayerRepository: MusicPlayerRepository
    lateinit var viewModel: PlayListViewModel

    @Before
    fun setup() {
        musicPlayerRepository = mockk(relaxed = true)
        viewModel = PlayListViewModel(musicPlayerRepository)
    }

    @Test
    fun `getPlaylists should return Success state when repository returns non-empty list`() = runTest {
        val expectedPlaylists = listOf(PlayList("Playlist 1"), PlayList("Playlist 2"))
        coEvery { musicPlayerRepository.getPlayList() } returns expectedPlaylists

        viewModel.getPlaylists()

        assertEquals(PlayListUiState.Success(expectedPlaylists), viewModel.playListUiState)
    }

    @Test
    fun `getPlaylists should return Empty state when repository returns empty list`() = runTest {
        val expectedPlaylists = emptyList<PlayList>()
        coEvery { musicPlayerRepository.getPlayList() } returns expectedPlaylists

        viewModel.getPlaylists()

        assertEquals(PlayListUiState.Empty, viewModel.playListUiState)
    }


    @Test
    fun `getPlaylists should return Error state when repository throws IOException`() = runTest {
        coEvery { musicPlayerRepository.getPlayList() } throws IOException()

        viewModel.getPlaylists()

        assertEquals(PlayListUiState.Error, viewModel.playListUiState)
    }
}