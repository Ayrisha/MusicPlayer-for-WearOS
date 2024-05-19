package com.example.musicplayer.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.dialog.Confirmation
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import kotlinx.coroutines.launch

@Composable
fun ConfirmationCard(
    navController: NavController,
    displayName: String?,
    email: String?,
    urlPhoto: String?,
    idToken: String?
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Confirmation(
        onTimeout = {
            coroutineScope.launch{
                val application = context.applicationContext as MusicApplication
                val musicRepository = application.container.musicPlayerRepository
                if (idToken != null) {
                    application.container.authInterceptor.setToken(idToken)
                }
                val tokens = musicRepository.auth()

                application.container.dataStore.updateRefreshToken(tokens.refreshToken)
                application.container.dataStore.updateAccessToken(tokens.accessToken)

                Log.d("idToken", idToken.toString())
                Log.d("accessToken", tokens.accessToken)
                Log.d("refreshToken", tokens.refreshToken)

                application.container.authInterceptor.setToken(tokens.accessToken)

                navController.navigate("menu") {
                    popUpTo("auth") {
                        inclusive = true
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
        durationMillis = 2000,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(urlPhoto)
                    .crossfade(true)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                onError = { errorState ->
                    val throwable = errorState.result.throwable
                    Log.e("AsyncImage", "Error: $throwable")
                },
                fallback = painterResource(R.drawable.baseline_person_24)
            )
            Text(
                text = "Привет, $displayName",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (email != null) {
                Text(
                    text = email,
                    color = Color.White.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
