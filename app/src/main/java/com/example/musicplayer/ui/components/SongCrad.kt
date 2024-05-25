package com.example.musicplayer.ui.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.session.MediaController
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.musicplayer.R
import com.example.musicplayer.media.MediaManager
import okhttp3.OkHttpClient

@Composable
fun SongCard(
    id: String?,
    title: String,
    artist: String?,
    img: String?,
    onClick: () -> Unit,
    mediaController: MediaController
) {

    val mediaManager = MediaManager(mediaController)

    Chip(
        modifier = Modifier
            .fillMaxWidth()
            .height(ChipDefaults.Height),
        onClick = {
            onClick()
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = Color(0xFF1C1B1F),
        ),
        border = ChipDefaults.chipBorder()
    ) {
        SongTitle(id = id, title = title, artist = artist, img = img, mediaManager = mediaManager)

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongTitle(
    id: String?,
    title: String,
    artist: String?,
    img: String?,
    mediaManager: MediaManager
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("play_waves.json"))

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        ){
            Log.d("SongTitle", "$img")
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(img)
                    .memoryCachePolicy(CachePolicy.READ_ONLY)
                    .diskCachePolicy(CachePolicy.READ_ONLY)
                    .build(),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                onError = { errorState ->
                    val throwable = errorState.result.throwable
                    Log.e("AsyncImage", "Error: $throwable")
                },
                fallback = painterResource(R.drawable.baseline_person_24),
            )
            if (id?.let { mediaManager.checkIsPlayingId(it) } == true) {
                Box(
                    modifier =  Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.8f))
                )
                LottieAnimation(
                    modifier = Modifier.size(24.dp),
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier.basicMarquee(),
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            artist?.let {
                Text(
                    text = it,
                    modifier = Modifier.basicMarquee(),
                    maxLines = 1,
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 10.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

    }
}
