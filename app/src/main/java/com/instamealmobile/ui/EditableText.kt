package com.instamealmobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditableText(modifier : Modifier = Modifier,text : String = "", placeholder : String = "", fontSize: TextUnit = 15.sp, maxLines : Int = 2, isEditing: Boolean=false, onSubmit : (String) -> Unit = {}) {
    var textState = remember { mutableStateOf(text) }
    EditableTextState(modifier,textState, placeholder,fontSize,maxLines, isEditing,onSubmit)
}

@Composable
fun EditableTextState(modifier : Modifier = Modifier,text : MutableState<String> = mutableStateOf(""), placeholder : String = "", fontSize: TextUnit = 15.sp, maxLines : Int = 2, isEditing: Boolean=false, onSubmit : (String) -> Unit = {}) {
    var isEditing by remember { mutableStateOf(isEditing) }

    if (isEditing || text.value.isEmpty()) {
        TextField(
            value = text.value,
            onValueChange = {
                text.value = it
                isEditing = true
                            },
            modifier = modifier
                .clip(RoundedCornerShape(20.dp))
            ,
            placeholder = {Text(placeholder)},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            maxLines = maxLines,
            textStyle = TextStyle(fontSize = fontSize),
            keyboardActions = KeyboardActions(
                onDone = {
                    isEditing = false
                    onSubmit(text.value)
                }
            )
        )
    } else {
        Text(
            text = text.value,
            maxLines = maxLines,
            fontSize=fontSize,
            modifier = modifier
                .clickable { isEditing = true }
        )
    }

}
