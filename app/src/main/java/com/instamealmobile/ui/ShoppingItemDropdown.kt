package com.instamealmobile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.instamealmobile.data.ApiState
import com.instamealmobile.viewModels.ShoppingListViewModel
import kotlinx.coroutines.launch

@Composable
fun ShoppingItemDropdown(lazyListState: LazyListState) {
    val viewModel : ShoppingListViewModel = viewModel()
    val shoppingListState by viewModel.shoppingList.collectAsState()
    var extended by remember { mutableStateOf(false) }
    val user = Firebase.auth
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val filteredSuggestions by remember {derivedStateOf {
        viewModel.suggestions.filter {
            viewModel.newItemText.value.isNotBlank()
                    && it.uppercase().contains(viewModel.newItemText.value.uppercase())
                    && if (shoppingListState is ApiState.Success) {
                        (shoppingListState as ApiState.Success).data.none { item ->
                            item.name.uppercase() == it.uppercase()
                        }
                    } else true
        }.sorted()
    }}

    Column {
        TextField(
            value = viewModel.newItemText.value,
            onValueChange = {viewModel.newItemText.value = it
                extended = filteredSuggestions.isNotEmpty()
                            },
            label = { Text("Add Item") },
            placeholder = { Text("New item for list...") },
            keyboardOptions = KeyboardOptions(imeAction= ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                val userInitial = user.currentUser?.displayName?.substring(0,1) ?: ""
                viewModel.addItemToList(viewModel.newItemText.value, userInitial) {
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(0)
                    }
                }
                viewModel.newItemText.value = ""
            }),
            singleLine = true,
            textStyle = TextStyle(fontSize = 20.sp),
            modifier = Modifier.fillMaxWidth().onFocusChanged({ focusState ->
                extended = focusState.isFocused
            })
        )
        DropdownMenu(expanded = extended, onDismissRequest = {extended = false},
            properties = PopupProperties(focusable = false)
            ) {
            filteredSuggestions.forEach {
                DropdownMenuItem(
                    text = {Text(it)},
                    onClick = {
                        extended = false
                        focusManager.clearFocus()
                        if (it.isNotBlank()) {
                            val userInitial = user.currentUser?.displayName?.substring(0,1) ?: ""
                            viewModel.addItemToList(it, userInitial) {
                                coroutineScope.launch {
                                    lazyListState.animateScrollToItem(0)
                                }
                            }
                            viewModel.newItemText.value = ""
                        }
                    }
                )
            }
        }
    }
}