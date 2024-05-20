package com.example.musicplayer.ui.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import kotlin.Result
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.activity.ConfirmationActivity
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.RevealValue
import androidx.wear.compose.foundation.rememberRevealState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.SwipeToRevealActionColors
import androidx.wear.compose.material.SwipeToRevealChip
import androidx.wear.compose.material.SwipeToRevealDefaults
import com.example.musicplayer.R
import com.example.musicplayer.ui.viewModel.PlaylistTracksViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalWearFoundationApi::class, ExperimentalWearMaterialApi::class)
@Composable
fun PlayListChip(
    navController: NavController,
    text: String,
    trackId: String? = null,
    onSwipe: () -> Unit
) {
    val tracksViewModel: PlaylistTracksViewModel = viewModel(factory = PlaylistTracksViewModel.Factory)
    val context = LocalContext.current
    val revealState = rememberRevealState()
    val coroutineScope = rememberCoroutineScope()
    val owner: LifecycleOwner = LocalLifecycleOwner.current

    SwipeToRevealChip(
        action = SwipeToRevealDefaults.action(
            icon = { Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete")},
            label = { Text(text = "Удалить") },
            onClick = {
                coroutineScope.launch {
                    delay(3000)
                    onSwipe()
                    revealState.animateTo(RevealValue.Covered)
                }
            }),
        revealState = revealState,
        undoAction = SwipeToRevealDefaults.undoAction(
            label = { Text(text = "Отмена", color = Color.White) },
            onClick = {coroutineScope.cancel()}),
        colors = SwipeToRevealActionColors(
            actionBackgroundColor = Color(238,103,92),
            actionContentColor = Color.Black,
            additionalActionBackgroundColor = Color.White,
            additionalActionContentColor = Color.White,
            undoActionBackgroundColor = Color(32,33,36),
            undoActionContentColor = Color.White
        )
    ) {
        Chip(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            onClick = {
                if (trackId != null) {
                    tracksViewModel.setTrack(text, trackId)

                    tracksViewModel.addTrackResult.observe(owner) { result ->
                        result.onSuccess { _ ->
                            val intent =
                                Intent(context, ConfirmationActivity::class.java).apply {
                                    putExtra(
                                        ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                                        ConfirmationActivity.SUCCESS_ANIMATION
                                    )
                                    putExtra(
                                        ConfirmationActivity.EXTRA_MESSAGE,
                                        "Песня добавлена"
                                    )
                                    putExtra(
                                        ConfirmationActivity.EXTRA_ANIMATION_DURATION_MILLIS,
                                        2000
                                    )
                                }
                            context.startActivity(intent)
                        }.onFailure { _ ->
                            val intent =
                                Intent(context, ConfirmationActivity::class.java).apply {
                                    putExtra(
                                        ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                                        ConfirmationActivity.FAILURE_ANIMATION
                                    )
                                    putExtra(
                                        ConfirmationActivity.EXTRA_MESSAGE,
                                        "Песня уже есть в \"$text\""
                                    )
                                    putExtra(
                                        ConfirmationActivity.EXTRA_ANIMATION_DURATION_MILLIS,
                                        2000
                                    )
                                }
                            context.startActivity(intent)
                        }
                        navController.popBackStack("play_screen", false)
                    }
                } else {
                    navController.navigate("playlisttracks/$text")
                }
            },
            colors = ChipDefaults.imageBackgroundChipColors(
                backgroundImagePainter = painterResource(id = R.drawable.outline_cloud_off_24),
                backgroundImageScrimBrush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF1C1B1F),
                        Color(0xFF1C1B1F),
                        Color(alpha = 0.1f, red = 0f, green = 0f, blue = 0f)
                    ),
                    startX = 0.0f,
                    endX = Float.POSITIVE_INFINITY
                )
            ),
            border = ChipDefaults.chipBorder(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )


            }
        }
    }
}

private fun startConfirmationActivity(animationType: Int, message: String, context: Context) {
    val activity = context as Activity
    val intent = Intent(activity, ConfirmationActivity::class.java).apply {
        putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, animationType)
        putExtra(ConfirmationActivity.EXTRA_MESSAGE, message)
        putExtra(ConfirmationActivity.EXTRA_ANIMATION_DURATION_MILLIS, 2000)
    }
    activity.startActivity(intent)
}