package com.example.musicplayer.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.wear.compose.material.Card
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.activity.ConfirmationActivity
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import com.example.musicplayer.MusicApplication
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.example.musicplayer.data.AppPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

private const val CLIENT_ID =
    "550470458277-2dvd7f06dd8npndqtc6o9kt9tcudjofn.apps.googleusercontent.com"


@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
@Composable
fun AuthScreen(
    navController: NavController,
    screen: String
) {
    var showDialogState by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val context = LocalContext.current
    var isSignedIn by remember { mutableStateOf(false) }
    val scope = CoroutineScope(newSingleThreadContext("name"))
    val googleSignInLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("GoogleSignInActivity", "Received Activity Result: ${result.resultCode}")
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val application = context.applicationContext as MusicApplication
                    val musicRepository = application.container.musicPlayerRepository

                    scope.launch {
                        musicRepository.sedCode(account.idToken.toString())
                    }

                    AppPreferences.isLogin = true
                    AppPreferences.isShow = false
                    isSignedIn = true

                    navController.navigate("menu_screen/${account.email}")
                    Log.d(
                        "GoogleSignInActivity",
                        "signInResult:success account=${account.idToken}"
                    )
                } catch (e: ApiException) {
                    showDialogState = true
                    Log.w("GoogleSignInActivity", "signInResult:failed code=${e.statusCode}")
                }
            } else {
                showDialogState = true
                Log.w(
                    "GoogleSignInActivity",
                    "signInResult:cancelled resultCode=${result.resultCode}"
                )
            }
        }

    if (showDialogState) {
        val activity = LocalContext.current as Activity
        val intent = Intent(activity, ConfirmationActivity::class.java).apply {
            putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.FAILURE_ANIMATION)
            putExtra(ConfirmationActivity.EXTRA_MESSAGE, "Не удалось выполнить вход")
            putExtra(ConfirmationActivity.EXTRA_ANIMATION_DURATION_MILLIS, 2000)
        }
        activity.startActivity(intent)
        showDialogState = false
    }

    Scaffold(
        positionIndicator = {
            PositionIndicator(lazyListState = listState)
        }
    ) {
        LazyColumn(
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement =  Arrangement.spacedBy(5.dp)
        ) {
            item {
                Text(
                    modifier = Modifier
                        .padding(top = 25.dp, start = 20.dp, end = 20.dp, bottom = 10.dp),
                    text = "Зарегестрируйтесь, чтобы иметь возможность сохранять любимые треки и создавать плейлисты",
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            item {
                CardSigIn(
                    imageVector = Icons.Rounded.AccountCircle,
                    text = "Войти через Google",
                    onClick = { signInWithGoogle(googleSignInLauncher, context) },
                    backgroundPainter = ColorPainter(Color(48, 79, 254))
                )
            }
            item {
                CardSigIn(
                    imageVector = Icons.Rounded.Clear,
                    text = "Продолжить без аккаунта",
                    onClick = { navController.navigate(screen) },
                    backgroundPainter = ColorPainter(Color(0xFF1C1B1F)),
                    padding = 20.dp
                )
            }
        }
    }
}

fun signInWithGoogle(googleSignInLauncher: ActivityResultLauncher<Intent>, context: Context) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(CLIENT_ID)
        .requestServerAuthCode(CLIENT_ID)
        .requestScopes(Scope("openid"))
        .requestEmail()
        .build()
    val signIn = GoogleSignIn.getClient(context, gso)
    signIn.signOut()
    val signInIntent = signIn.signInIntent
    googleSignInLauncher.launch(signInIntent)
}

@Composable
fun CardSigIn(
    imageVector: ImageVector,
    text: String,
    onClick: () -> Unit,
    backgroundPainter: ColorPainter,
    padding: Dp = 0.dp) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, bottom = padding),
        onClick = onClick,
        backgroundPainter = backgroundPainter,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = "icon",
                tint = Color.White
            )
            Text(
                text = text,
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}

