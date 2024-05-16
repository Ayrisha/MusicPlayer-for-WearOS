package com.example.musicplayer.ui.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import com.example.musicplayer.MusicApplication
import com.example.musicplayer.auth.GoogleSignInHelper
import com.example.musicplayer.ui.components.CardSigIn
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun AuthScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val googleSignInHelper = GoogleSignInHelper(context)

    val listState = rememberLazyListState()

    val googleSignInLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        val account = task.getResult(ApiException::class.java)

                        Log.d(
                            "GoogleSignInActivity",
                            "IdToken:${account.idToken}"
                        )

                        Log.d(
                            "GoogleSignInActivity",
                            "IdToken:${account.serverAuthCode}"
                        )

                        account.idToken?.let {
                            val appContainer = (context.applicationContext as MusicApplication).container
                            appContainer.basicAuthInterceptor.setToken(it)
                        }

                        val displayName = Uri.encode(account.displayName ?: "")
                        val email = Uri.encode(account.email ?: "")
                        val photoUrl = Uri.encode(account.photoUrl?.toString() ?: "")

                        navController.navigate("confirmation/$displayName/$email/$photoUrl")

                    } catch (e: ApiException) {
                        Log.w("GoogleSignInActivity", "signInResult:failed code=${e.statusCode}")
                    }
                } else {
                    Log.w(
                        "GoogleSignInActivity",
                        "signInResult:cancelled resultCode=${result.resultCode}"
                    )
                }
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
                            googleSignInHelper.signIn(googleSignInLauncher)
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

