package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instamealmobile.makeLongList
import com.instamealmobile.ui.CheckItem
import com.instamealmobile.viewModels.ShoppingListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.LaunchedEffect
import com.instamealmobile.viewModels.ApiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListPage(onDismiss : () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    var newItemText by remember { mutableStateOf("") }
    val viewModel: ShoppingListViewModel =  viewModel()
    val shoppingListState by viewModel.shoppingList.observeAsState()
    val height = 350.dp

    LaunchedEffect(Unit) {
        viewModel.fetchShoppingList()
    }

    ModalBottomSheet(onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle()}
    ) {
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
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black, fontSize = 20.sp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            when (shoppingListState) {
                is ApiState.Loading -> {
                    CircularProgressIndicator()
                }
                is ApiState.Success -> {
                    val shoppingList = (shoppingListState as ApiState.Success<List<String>>).data
                    LazyColumn(
                        modifier = Modifier
                            .height(height)
                            .fillMaxWidth()
                    ) {
                        items(shoppingList) { item ->
                            CheckItem(name = item, color = Color.Black, FontFamily.Default)
                        }
                    }
                }
                is ApiState.Error -> {
                    val error = (shoppingListState as ApiState.Error).message
                    Text(error)
                }

                null -> TODO()
            }


        }
    }
}
