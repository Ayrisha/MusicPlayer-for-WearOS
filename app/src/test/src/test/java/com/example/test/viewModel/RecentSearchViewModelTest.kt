package com.example.musicplayer.ui.viewModel

import com.example.musicplayer.MusicApplication
import com.example.musicplayer.datastore.MyDataStore
import com.example.musicplayer.ui.viewModel.state.RecentSearchState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test


class RecentSearchViewModelTest {

    lateinit var musicApplication: MusicApplication
    private lateinit var viewModel: RecentSearchViewModel
    private lateinit var myDataStore: MyDataStore

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        musicApplication = mockk(relaxed = true)
        myDataStore = mockk(relaxed = true)
        viewModel = RecentSearchViewModel(musicApplication)
    }

    @Test
    fun `setSearch should update recent searches`() = runTest {
        val query = "Test query"
        val updatedSearches = listOf("Search 1", "Search 2", query)
        coEvery { myDataStore.updateRecentSearches(any()) } returns Unit

        viewModel.setSearch(query)

        assertEquals(RecentSearchState.Success(updatedSearches), viewModel.recentSearchState)
    }

    @Test
    fun `setSearch should not update recent searches if query is blank`() = runTest {
        viewModel.setSearch("")
        assertEquals(RecentSearchState.Empty, viewModel.recentSearchState)
    }

    @Test
    fun `removeRecentSearch should remove the search query`() = runTest {

        val queryToRemove = "Search to remove"
        val initialSearches = listOf("Search 1", queryToRemove, "Search 2")
        val updatedSearches = listOf("Search 1", "Search 2")
        coEvery { myDataStore.updateRecentSearches(any()) } returns Unit

        viewModel.updateRecentSearchState(initialSearches)
        viewModel.removeRecentSearch(queryToRemove)

        assertEquals(RecentSearchState.Success(updatedSearches), viewModel.recentSearchState)

        coVerify { viewModel.updateRecentSearchState(updatedSearches) }
    }
}