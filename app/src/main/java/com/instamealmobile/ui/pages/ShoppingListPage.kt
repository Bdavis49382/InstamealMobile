package com.instamealmobile.ui.pages

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instamealmobile.ui.CheckItem
import com.instamealmobile.viewModels.ShoppingListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.input.ImeAction
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.ShoppingItem
import com.instamealmobile.data.SmallShoppingItem
import com.instamealmobile.ui.placeholders.ShoppingListPlaceholder
import kotlinx.coroutines.launch

@Composable
fun ShoppingListPage(lazyListState: LazyListState) {
    var newItemText by remember { mutableStateOf("") }
    val viewModel: ShoppingListViewModel =  viewModel()
    val shoppingListState by viewModel.shoppingList.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetchShoppingList()
    }

    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            TextField(
                value = newItemText,
                onValueChange = {newItemText = it},
                label = { Text("Add Item") },
                placeholder = { Text("New item for list...") },
                keyboardOptions = KeyboardOptions(imeAction= ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.addItemToList(newItemText)
                    newItemText = ""
                }),
                singleLine = true,
                textStyle = TextStyle(fontSize = 20.sp),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Crossfade(targetState = shoppingListState, label = "ContentSwitch") { screenState ->
            when (screenState) {
                is ApiState.Loading -> {
                    ShoppingListPlaceholder()
                }

                is ApiState.Success<*> -> if (shoppingListState is ApiState.Success){
                    val shoppingList =
                        (shoppingListState as ApiState.Success<List<ShoppingItem>>).data
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    ) {
                        items(shoppingList.reversed().sortedBy { it.checked }, key = {it.index}) { item ->
                            CheckItem(
                                modifier = Modifier.animateItem(),
                                shoppingItem = item,
                                editMethod = fun(text: String) {
                                    val newShoppingItem = SmallShoppingItem(item)
                                    newShoppingItem.name = text
                                    viewModel.editItem(
                                        item.index,
                                        newShoppingItem
                                    )
                                },
                                checkMethod = {
                                    if(!item.checked) {
                                        coroutineScope.launch {
                                            lazyListState.animateScrollToItem(0)
                                        }
                                    }
                                    viewModel.checkItem(item.index)
                                }
                            )
                        }
                    }
                }

                is ApiState.Error -> if (shoppingListState is ApiState.Error){
                    val error = (shoppingListState as ApiState.Error).message
                    Text(error)
                }

                else -> {}
            }
        }
    }
}
