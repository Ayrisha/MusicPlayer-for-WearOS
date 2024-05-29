package com.example.musicplayer.ui.components.playlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.musicplayer.ui.screens.Routes
import com.example.musicplayer.ui.viewModel.PlaylistTracksViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PlayListChip(
    navController: NavController,
    text: String,
    trackId: String? = null,
) {
    val tracksViewModel: PlaylistTracksViewModel =
        viewModel(factory = PlaylistTracksViewModel.Factory)

    val context = LocalContext.current

    val owner: LifecycleOwner = LocalLifecycleOwner.current

    Chip(
        modifier = Modifier
            .fillMaxWidth()
            .height(ChipDefaults.Height),
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
                                    "Песня уже есть в $text"
                                )
                                putExtra(
                                    ConfirmationActivity.EXTRA_ANIMATION_DURATION_MILLIS,
                                    2000
                                )
                            }
                        context.startActivity(intent)
                    }
                    navController.popBackStack()
                }
            } else {
                navController.navigate(Routes.PlayListTracksScreen + "/$text")
            }
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = Color(0xFF1C1B1F)
        ),
        border = ChipDefaults.chipBorder(),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 15.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "item image",
                modifier = Modifier.size(25.dp),
                tint = Color.White
            )
        }
    }
}