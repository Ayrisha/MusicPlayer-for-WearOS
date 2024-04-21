package com.example.musicplayer.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.musicplayer.TrackUiState
import com.example.musicplayer.data.Track

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongList(
    listState: LazyListState,
    listTrack: List<Track>,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(
            start = 10.dp,
            end = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item(
            contentType = "header"
        ) {
            Text(
                text = "Популярное",
                color = Color.White)
        }
        items(listTrack) { item ->
            SongCard(
                navController = navController,
                id = item.id,
                title = item.title,
                artist = item.artist,
                img = item.imgLink
            )
        }
    }
}