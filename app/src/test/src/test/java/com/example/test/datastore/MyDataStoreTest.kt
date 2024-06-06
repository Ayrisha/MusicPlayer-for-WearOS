package com.example.musicplayer.datastore

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MyDataStoreTest {

    private var testContext = InstrumentationRegistry.getInstrumentation().context
    private var myDataStore = MyDataStore(testContext)

    @Test
    fun `test Update Recent Searches`() = runTest {
        val searches = listOf("search1", "search2")

        myDataStore.updateRecentSearches(searches)

        assertEquals(searches, myDataStore.recentSearches.first())
    }

    @Test
    fun `test Update Refresh Token`() = runTest {
        val refreshToken = "new_refresh_token"

        myDataStore.updateRefreshToken(refreshToken)

        assertEquals(refreshToken, myDataStore.refreshToken.first())
    }

    @Test
    fun `test Update Access Token`() = runTest {
        val accessToken = "new_access_token"

        myDataStore.updateAccessToken(accessToken)

        assertEquals(accessToken, myDataStore.accessToken.first())
    }

    @Test
    fun `test Clear Tokens`() = runTest {
        myDataStore.clearTokens()

        assert(myDataStore.refreshToken.first() == null)
        assert(myDataStore.accessToken.first() == null)
    }

}