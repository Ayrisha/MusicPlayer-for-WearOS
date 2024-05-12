package com.example.musicplayer.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.activity.ConfirmationActivity
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.auth.AuthGoogleViewModel
import com.example.musicplayer.datastore.DataStoreManager
import com.example.musicplayer.ui.components.CardSigIn
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private const val CLIENT_ID =
    "550470458277-2dvd7f06dd8npndqtc6o9kt9tcudjofn.apps.googleusercontent.com"


@OptIn(DelicateCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AuthScreen(
    navController: NavController,
    screen: String
) {

    var showDialogState by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val viewModel: AuthGoogleViewModel = viewModel(factory = AuthGoogleViewModel.Factory)
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

                    GlobalScope.launch {
                        DataStoreManager.getInstance().updateIsCompleted(true)
                        DataStoreManager.getInstance().isCompleted.collect { isCompleted ->
                            if (isCompleted) {
                                Log.d("DataAuth", "True")
                            } else {
                                Log.d("DataAuth", "False")
                            }
                        }
                        DataStoreManager.getInstance().updateIsShow(false)
                        //musicRepository.sedCode(account.idToken.toString())
                    }

                    Log.d(
                        "GoogleSignInActivity",
                        "signInResult:success account=${account.idToken}"
                    )
                    navController.navigate("menu/${account.email}") {
                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
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
            putExtra(
                ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                ConfirmationActivity.FAILURE_ANIMATION
            )
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
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item {
                Text(
                    modifier = Modifier
                        .padding(top = 25.dp, start = 20.dp, end = 20.dp, bottom = 10.dp),
                    text = "Зарегестрируйтесь,\n чтобы иметь возможность сохранять любимые треки и создавать плейлисты",
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            item {
                CardSigIn(
                    imageVector = Icons.Rounded.AccountCircle,
                    text = "Войти через Google",
                    onClick = {
                        //viewModel.startAuthFlow(context)
                        signInWithGoogle(googleSignInLauncher, context)
                    },
                    backgroundPainter = ColorPainter(Color(48, 79, 254))
                )
            }
            item {
                CardSigIn(
                    imageVector = Icons.Rounded.Clear,
                    text = "Продолжить без аккаунта",
                    onClick = {
                        navController.navigate("menu") {
                            popUpTo("auth") {
                                inclusive = true
                            }
                        }
                    },
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
        .requestScopes(Scope("openid"), Scope("email"), Scope("profile"))
        .requestEmail()
        .build()
    val signIn = GoogleSignIn.getClient(context, gso)
    signIn.signOut()
    val signInIntent = signIn.signInIntent
    googleSignInLauncher.launch(signInIntent)
}

