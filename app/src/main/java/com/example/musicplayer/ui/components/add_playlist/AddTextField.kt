package com.example.musicplayer.ui.components.add_playlist


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.ui.components.auth.CardSigIn

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, name = "WEAR_OS_SMALL_ROUND")
@Preview(device = Devices.WEAR_OS_SQUARE, name = "WEAR_OS_SQUARE")
@Preview(device = Devices.WEAR_OS_LARGE_ROUND, name = "WEAR_OS_LARGE_ROUND")
@Composable
fun PreviewAddTextField() {
    AddTextField(rememberNavController(), "Песня", {}, {})
}

@Composable
fun AddTextField(
    navController: NavController,
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit
) {
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
                navController.popBackStack()
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