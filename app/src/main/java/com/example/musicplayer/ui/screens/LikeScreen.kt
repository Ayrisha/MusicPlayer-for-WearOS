package com.example.musicplayer.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.rememberRevealState
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.SwipeToRevealAction
import androidx.wear.compose.material.SwipeToRevealCard
import androidx.wear.compose.material.SwipeToRevealDefaults
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.edgeSwipeToDismiss
import androidx.wear.compose.material.rememberSwipeToDismissBoxState
import com.example.musicplayer.ui.components.LikeCard
import com.example.musicplayer.ui.components.PlayListChip
import kotlinx.coroutines.delay

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalWearMaterialApi::class, ExperimentalWearFoundationApi::class)
@Composable
fun LikeScreen() {
    Box(
        modifier = Modifier.padding(
            top = 30.dp,
            start = 10.dp,
            end = 10.dp,
            bottom = 30.dp
        )
    ) {
        val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
        val revealState = rememberRevealState()
        val interactionSource = remember { MutableInteractionSource() }
        var undoVisible by remember { mutableStateOf(false) }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SwipeToRevealCard(
                revealState = revealState,
                modifier = Modifier
                    .fillMaxWidth()
                    .edgeSwipeToDismiss(swipeToDismissBoxState),
                action = SwipeToRevealAction(
                    icon = {
                        Icon(SwipeToRevealDefaults.Delete, "Delete")
                    },
                    label = {
                        Text("Delete")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    interactionSource = interactionSource,
                    onClick = {
                        undoVisible = true
                    }
                ),
                undoAction =
                SwipeToRevealAction(
                    icon = {},
                    label = {
                        Text("Undo")
                    },
                    modifier = Modifier.fillMaxSize(),
                    interactionSource = interactionSource,
                    onClick = {
                        undoVisible = false
                    }
                ),
            ) {
                if (undoVisible) {
                    LaunchedEffect(Unit) {
                        delay(3000)
                        Log.w("LikeScreen", "Delete")
                        undoVisible = false
                    }
                }
                LikeCard()
            }
            PlayListChip()
        }
    }
}

