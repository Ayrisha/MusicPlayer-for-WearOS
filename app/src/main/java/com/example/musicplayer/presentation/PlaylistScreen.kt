package com.example.musicplayer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TitleCard
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import com.example.musicplayer.presentation.ui.theme.MusicPlayerTheme

class PlaylistScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MusicPlayerTheme {
                val listState = rememberScalingLazyListState()

                Scaffold(
                    timeText = {
                        TimeText(modifier = Modifier.scrollAway(listState))
                    },
                    vignette = {
                        Vignette(vignettePosition = VignettePosition.TopAndBottom)
                    },
                    positionIndicator = {
                        PositionIndicator(
                            scalingLazyListState = listState
                        )
                    }
                ) {
                    ScalingLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        autoCentering = AutoCenteringParams(itemIndex = 0)
                    ) {
                        item { TextItem("Загруженные", "descriptions") }
                        item { TextItem("Все", "descriptions") }
                        item { TextItem("Понравившиеся", "descriptions") }
                        item { TextItem("Понравившиеся", "descriptions") }
                        item { TextItem("Понравившиеся", "descriptions") }
                        item { TextItem("Понравившиеся", "descriptions") }
                        item { TextItem("Понравившиеся", "descriptions") }
                        item { TextItem("Понравившиеся", "descriptions") }
                        item { TextItem("Понравившиеся", "descriptions") }
                        item { TextItem("Понравившиеся", "descriptions") }
                        item { TextItem("Понравившиеся", "descriptions") }
                        item { TextItem("Понравившиеся", "descriptions") }

                    }
                }
            }
        }

    }

    @Composable
    fun TextItem(contents: String, descriptions: String) {
        TitleCard(
            onClick = {},
            title = { Text(contents) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 1.dp)
        ) {
            Text(descriptions)
        }
    }
}

