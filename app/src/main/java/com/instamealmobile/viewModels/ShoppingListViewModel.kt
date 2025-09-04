package com.instamealmobile.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.ShoppingItem
import com.instamealmobile.data.SmallShoppingItem
import com.instamealmobile.network.ShoppingListService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class ShoppingListViewModel @Inject constructor(private val apiService: ShoppingListService): ViewModel() {
    private val _shoppingList = MutableStateFlow<ApiState<List<ShoppingItem>>>(ApiState.Loading)
    val shoppingList: MutableStateFlow<ApiState<List<ShoppingItem>>> = _shoppingList
    val localList = mutableStateListOf<ShoppingItem>()
    val moves = mutableListOf<Pair<Int,Int>>()
    var scope = viewModelScope

    fun fetchShoppingList() {
        _shoppingList.value = ApiState.Loading
        scope.launch {
            try {
                val response = apiService.getShoppingList()
                _shoppingList.value = ApiState.Success(response)
                localList.clear()
                localList.addAll((shoppingList.value as ApiState.Success).data)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun addItemToList(text: String, userInitial: String, scrollToTop: () -> Unit) {
        scope.launch {
            try {
                localList.add(0,ShoppingItem(name=text, user_initial = userInitial))
                scrollToTop()
                val response = apiService.postShoppingList(ShoppingItem(
                    name=text))
                apply(response, scrollToTop)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to update shopping list: ${e.message}")
            }
        }
    }

    fun apply(newList: List<ShoppingItem>, scrollToTop: () -> Unit) {
        //    If there are any differences, apply the newList, otherwise leave it
        for (item in localList.zip(newList)) {
            if (item.first.name != item.second.name) {
                _shoppingList.value = ApiState.Success(newList)
                localList.clear()
                localList.addAll(newList)
                scrollToTop()
                return
            }
        }
        _shoppingList.value = ApiState.Success(localList)
    }

    fun checkItem(index: Int) {
        scope.launch {
            try {
                if (!localList[index].checked) {
                    // Send to the front of the back of the list
                    var toIndex = localList.indexOfFirst { it.checked}
                    toIndex = if (toIndex == -1) localList.size - 1 else toIndex - 1
                    localList.add(toIndex, localList.removeAt(index).copy(checked = true))
                } else {
                    // Send to the back of the front of the list
                    var toIndex = localList.indexOfLast { !it.checked}
                    toIndex = if (toIndex == -1) 0 else if (toIndex == localList.size - 1) toIndex - 1 else toIndex + 1
                    localList.add(toIndex, localList.removeAt(index).copy(checked = false))
                }
                val response = apiService.checkItem(index)
                _shoppingList.value = ApiState.Success(response)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to check item: ${e.message}")
            }
        }
    }

    fun editItem(index: Int,item: SmallShoppingItem) {
        scope.launch {
            try {
                val response = apiService.editItem(index, item)
                localList[index] = localList[index].copy(name = item.name)
                _shoppingList.value = ApiState.Success(response)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to edit item: ${e.message}")
            }
        }
    }

    fun moveItem(fromIndex: Int, toIndex: Int) {
        scope.launch {
            try {
                localList.add(toIndex, localList.removeAt(fromIndex))
                val index = moves.indexOfFirst { it.second == fromIndex}
                if (index != -1) {
                    moves[index] = moves[index].copy(second = toIndex)
                } else {
                    moves.add(Pair(fromIndex, toIndex))
                }
                delay(500)
                // if there is a move that ends where I end, perform it
                val moveIndex = moves.indexOfFirst { it.second == toIndex }
                if (moveIndex != -1 && moves.isNotEmpty()) {
                    val response = apiService.moveItem(moves[moveIndex].first,moves[moveIndex].second)
                    moves.removeAt(moveIndex)
                    _shoppingList.value = ApiState.Success(response)
                }

            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to move item: ${e.message}")

            }

        }
    }
}