package com.instamealmobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditableText(modifier : Modifier = Modifier,text : String = "", placeholder : String = "", fontSize: TextUnit = 15.sp, maxLines : Int = 2, isEditing: Boolean=false, precursor: String ="", onSubmit : (String) -> Unit = {}) {
    var textState = remember { mutableStateOf(text) }
    EditableTextState(modifier,textState, placeholder,fontSize,maxLines, isEditing,precursor,onSubmit=onSubmit)
}

@Composable
fun EditableTextState(modifier : Modifier = Modifier,text : MutableState<String> = mutableStateOf(""), placeholder : String = "", fontSize: TextUnit = 15.sp, maxLines : Int = 2, isEditing: Boolean=false, precursor: String="", errorCondition: Boolean = false,errorMessage: String = "Failed Validation", onSubmit : (String) -> Unit = {}) {
    var startingText by remember { mutableStateOf("")}
    var isEditing by remember { mutableStateOf(isEditing) }
    var wasFocused by remember { mutableStateOf(false)}
    var textFieldValueState by remember { mutableStateOf(
        TextFieldValue(text=text.value, selection = TextRange.Zero)
        )
    }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        startingText = text.value
    }
    LaunchedEffect(text.value) {
        textFieldValueState = textFieldValueState.copy(text = text.value)
    }
    LaunchedEffect(isEditing) {
        if (isEditing) {
            focusRequester.requestFocus()
            textFieldValueState = textFieldValueState.copy(
                selection = TextRange(textFieldValueState.text.length)
            )
        }
    }

    if (isEditing || text.value.isEmpty()) {
        TextField(
            value = textFieldValueState,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
            onValueChange = {
                if (text.value.isEmpty()) {
                    isEditing = true
                }
                text.value = it.text
                textFieldValueState = it
                            },
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                if (!focusState.isFocused && wasFocused) {
                    isEditing = false
                }
                wasFocused = focusState.isFocused
            },
            shape = RoundedCornerShape(20.dp),
            placeholder = {Text(placeholder)},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            maxLines = maxLines,
            isError = errorCondition,
            supportingText = {
                if (errorCondition) {
                    Text(errorMessage)
                }
            },
            textStyle = TextStyle(fontSize = fontSize),
            keyboardActions = KeyboardActions(
                onDone = {
                    isEditing = false
                    if (text.value != startingText) {
                        onSubmit(text.value)
                    }
                }
            )
        )
    } else {
        Text(
            text = precursor + text.value,
            maxLines = maxLines,
            fontSize=fontSize,
            modifier = modifier
                .fillMaxWidth()
                .clickable { isEditing = true }
        )
    }

}
