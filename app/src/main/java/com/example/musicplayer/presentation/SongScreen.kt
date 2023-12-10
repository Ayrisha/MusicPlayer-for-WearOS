package com.example.musicplayer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
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

class SongScreen : ComponentActivity() {
    @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val player = ExoPlayer.Builder(this)
            .setSeekForwardIncrementMs(5000L)
            .setSeekBackIncrementMs(5000L)
            .build()
        val viewModel = MyViewModel(player)

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
                        item {SongItem(contents = player.getMediaItemAt(0).mediaId)}
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
                .fillMaxWidth()
                .padding(bottom = 1.dp)
        ) {
        }
    }
}
