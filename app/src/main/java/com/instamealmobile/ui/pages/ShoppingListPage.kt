package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instamealmobile.ui.CheckItem
import com.instamealmobile.viewModels.ShoppingListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.input.ImeAction
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.ShoppingItem
import com.instamealmobile.data.SmallShoppingItem
import com.instamealmobile.ui.placeholders.ShoppingListPlaceholder

@Composable
fun ShoppingListPage() {
    var newItemText by remember { mutableStateOf("") }
    val viewModel: ShoppingListViewModel =  viewModel()
    val shoppingListState by viewModel.shoppingList.observeAsState()

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
                textStyle = TextStyle(color = Color.Black, fontSize = 20.sp),
                modifier = Modifier.fillMaxWidth()
            )
        }
        when (shoppingListState) {
            is ApiState.Loading -> {
                ShoppingListPlaceholder()
            }
            is ApiState.Success<*> -> {
                val shoppingList = (shoppingListState as ApiState.Success<List<ShoppingItem>>).data
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    itemsIndexed(shoppingList.reversed()) { index,item ->
                        CheckItem(shoppingItem = item,
                            editMethod = fun(text: String){
                                val newShoppingItem = SmallShoppingItem(item)
                                newShoppingItem.name = text
                                viewModel.editItem(shoppingList.size - index - 1, newShoppingItem)
                            },
                            checkMethod = {
                                viewModel.checkItem(shoppingList.size - index - 1)
                            }
                        )
                    }
                }
            }
            is ApiState.Error -> {
                val error = (shoppingListState as ApiState.Error).message
                Text(error)
            }

            else -> {}
        }


    }
}
