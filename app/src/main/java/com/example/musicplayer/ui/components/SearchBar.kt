package com.example.musicplayer.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ContentAlpha

@Composable
fun SearchBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?
) {
    TextField(
        modifier = Modifier
            .size(155.dp, 85.dp)
            .padding(top = 30.dp, bottom = 10.dp),
        value = text,
        onValueChange = {
            onTextChange(it)
        },
        singleLine = true,
        leadingIcon = {
            IconButton(
                modifier = Modifier.size(15.dp)
                    .alpha(ContentAlpha.disabled),
                onClick = { onSearchClicked(text) }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )

            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchClicked(text)
                keyboardController?.hide()
            }
        ),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
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
            focusedIndicatorColor =  Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            cursorColor = Color.White
        )
    )
}
