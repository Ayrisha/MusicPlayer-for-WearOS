package com.example.musicplayer.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AddTextField(
    navController: NavController,
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?
){
    Column (
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
            text = "Введите название плейлиста",
            textAlign = TextAlign.Center,
            color = Color.White
        )
        TextField(
            value = text,
            modifier = Modifier
                .fillMaxWidth(),
            onValueChange = {
                onTextChange(it)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSearchClicked(text)
                    keyboardController?.hide()
                    navController.popBackStack("playlists", false)
                }
            ),
            textStyle = TextStyle.Default.copy(fontSize = 10.sp),
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                disabledTextColor = Color.White,
                errorTextColor = Color.White,
                focusedContainerColor = Color(0xFF1C1B1F),
                unfocusedContainerColor = Color(0xFF1C1B1F),
                disabledContainerColor = Color(0xFF1C1B1F),
                errorContainerColor = Color(0xFF1C1B1F),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                cursorColor = Color.White
            )
        )
    }
}