package com.example.musicplayer.ui.components.auth

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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.dialog.Confirmation
import androidx.wear.compose.material.dialog.DialogDefaults
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.R
import com.example.musicplayer.ui.components.add_playlist.AddTextField
import com.example.musicplayer.ui.screens.NavigationDestinations
import kotlinx.coroutines.launch

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, name = "WEAR_OS_SMALL_ROUND")
@Preview(device = Devices.WEAR_OS_SQUARE, name = "WEAR_OS_SQUARE")
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, name = "WEAR_OS_LARGE_ROUND")
@Composable
fun PreviewUserInfoScreen() {
    UserInfoScreen("1", "name", "email", rememberNavController(), " ")
}

@Composable
fun UserInfoScreen(
    idToken: String?,
    displayName: String?,
    email: String?,
    navController: NavController,
    photoUrl: String?
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Confirmation(
        onTimeout = {
            coroutineScope.launch{
                Log.d("ConfirmationCard", "IDToken: ${idToken}")

                val application = context.applicationContext as MusicApplication

                val musicRepository = application.container.musicPlayerRepository

                if (idToken != null) {
                    Log.d("ConfirmationCard", "Set token IDToken: $idToken")
                    application.container.authInterceptor.setToken(idToken)
                }

                val tokens =
                    musicRepository.auth().also { authTokens ->
                        Log.d("AuthInterceptor", "Received tokens: $authTokens")
                    }


                Log.d("ConfirmationCard token save", "Refresh Token: ${tokens.refreshToken}, Access Token: ${tokens.accessToken}")
                application.container.dataStore.updateRefreshToken(tokens.refreshToken)
                application.container.dataStore.updateAccessToken(tokens.accessToken)

                application.container.authInterceptor.setToken(tokens.accessToken)

                navController.navigate(NavigationDestinations.Menu) {
                    popUpTo(NavigationDestinations.Auth) {
                        inclusive = true
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
        durationMillis = DialogDefaults.ShortDurationMillis,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoUrl)
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