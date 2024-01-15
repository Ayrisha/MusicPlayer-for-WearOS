package com.example.musicplayer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TitleCard
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition

@OptIn(ExperimentalMaterial3Api::class, ExperimentalWearFoundationApi::class)
@Preview(device = Devices.WEAR_OS_LARGE_ROUND)
@Composable
fun SearchScreen() {
    val listState = rememberLazyListState()

    val searchText = remember {
        mutableStateOf("")
    }

    val resultList = remember{
        mutableStateOf(listOf("sdc", "csc"))
    }

    val isActive = remember {
        mutableStateOf(false)
    }

    Scaffold(
        timeText = {
            TimeText()
        },
        vignette = {
            Vignette(vignettePosition = VignettePosition.Bottom)
        },
        positionIndicator = {
            PositionIndicator(lazyListState = listState)
        }
    ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchBar(
                    modifier = Modifier
                        .size(155.dp, 90.dp)
                        .padding(top = 30.dp, bottom = 10.dp),
                    colors = SearchBarDefaults.colors(
                        containerColor = Color(0xFF1C1B1F),
                        inputFieldColors = TextFieldDefaults.colors(focusedTextColor = Color.White)
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "search icon",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )},
                    query = searchText.value,
                    onQueryChange = { text ->
                        searchText.value = text
                    },
                    onSearch = { text -> isActive.value = false },
                    active = false,
                    onActiveChange = {
                        isActive.value = it
                    })
                {
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(
                        start = 10.dp,
                        end = 10.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(resultList.value){ item ->
                        TitleCard(
                            onClick = {},
                            title = { Text(item) },
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                        }
                    }
                }
            }
    }
}

@Composable
fun SongItem(contents: String) {
    TitleCard(
        onClick = {},
        title = { Text(contents) },
        modifier = Modifier
            .fillMaxSize()
    ) {
    }
}
