package com.instamealmobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp

@Composable
fun EditableText(modifier : Modifier = Modifier,text : String = "", placeholder : String = "", maxLines : Int = 2, onSubmit : (String) -> Unit = {}) {
    var text by remember { mutableStateOf(text) }
    var isEditing by remember { mutableStateOf(false) }

    if (isEditing) {
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = modifier,
            placeholder = {Text(placeholder)},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            maxLines = maxLines,
            keyboardActions = KeyboardActions(
                onDone = {
                    isEditing = false
                    onSubmit(text)
                }
            )
        )
    } else {
        Text(
            text = text,
            maxLines = 2,
            fontSize=20.sp,
            modifier = modifier
                .clickable { isEditing = true }
        )
    }
}
