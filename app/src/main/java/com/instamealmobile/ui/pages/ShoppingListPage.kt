package com.instamealmobile.ui.pages

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.instamealmobile.ui.CheckItem
import com.instamealmobile.viewModels.ShoppingListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.ShoppingItem
import com.instamealmobile.data.SmallShoppingItem
import com.instamealmobile.ui.ShoppingItemDropdown
import com.instamealmobile.ui.placeholders.ShoppingListPlaceholder
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun ShoppingListPage(lazyListState: LazyListState) {
    val viewModel: ShoppingListViewModel =  viewModel()
    val shoppingListState by viewModel.shoppingList.collectAsState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) {  from, to ->
        viewModel.moveItem(from.index, to.index)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchShoppingList()
        viewModel.getSuggestions()
    }

    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            ShoppingItemDropdown(lazyListState)
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
                        horizontalAlignment = Alignment.CenterHorizontally,
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    ) {
                        itemsIndexed(viewModel.localList, key = {index,item -> item.id?:item.name}) { index,item ->
                            ReorderableItem(reorderableLazyListState, key=item.id?:item.name) { isDragging ->
                                val shadowElevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                                CheckItem(
                                    this,
                                    modifier = Modifier.animateItem().shadow(shadowElevation),
                                    shoppingItem = item,
                                    editMethod = fun(text: String) {
                                        val newShoppingItem = SmallShoppingItem(item)
                                        newShoppingItem.name = text
                                        viewModel.editItem(
                                            index,
                                            newShoppingItem
                                        )
                                    },
                                    checkMethod = {
                                        viewModel.checkItem(index)
                                    }
                                )
                            }
                        }
                        item {
                            if (shoppingList.isEmpty()) {
                                Text("No Shopping Left To Be Done! Add Items For Your Next Shopping Trip.", modifier = Modifier.padding(horizontal = 30.dp))
                            }
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
