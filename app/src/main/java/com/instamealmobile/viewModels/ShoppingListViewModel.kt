package com.instamealmobile.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
import kotlin.random.Random


@HiltViewModel
class ShoppingListViewModel @Inject constructor(private val apiService: ShoppingListService): ViewModel() {
    private val _shoppingList = MutableStateFlow<ApiState<List<ShoppingItem>>>(ApiState.Loading)
    val shoppingList: MutableStateFlow<ApiState<List<ShoppingItem>>> = _shoppingList
    val localList = mutableStateListOf<ShoppingItem>()
    val latestMove = mutableStateOf<Pair<String,Int>?>(null)
    val lastChange = mutableStateOf(0)
    var newItemText = mutableStateOf("")
    var suggestions = mutableStateListOf<String>()
    var scope = viewModelScope

    fun fetchShoppingList() {
        _shoppingList.value = ApiState.Loading
        scope.launch {
            try {
                val response = apiService.getShoppingList()
                apply(response)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun getSuggestions() {
        scope.launch {
            try {
                suggestions.clear()
                suggestions.addAll(apiService.getSuggestions())
            } catch (e: Exception) {
                Log.e("Suggestions","suggestions did not load correctly: ${e.message}")
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

    fun apply(newList: List<ShoppingItem>, scrollToTop: () -> Unit = {}) {
        //    If there are any differences, apply the newList, otherwise leave it
        if (localList.size != newList.size) {
            localList.clear()
            localList.addAll(newList)
            scrollToTop()
        } else {
            localList.zip(newList).forEachIndexed { index, item ->
                if (item.first.id != item.second.id || item.first.checked != item.second.checked || item.first.name != item.second.name) {
                    localList[index] = item.second
                }
            }
        }
        _shoppingList.value = ApiState.Success(localList)
    }

    fun checkItem(index: Int) {
        scope.launch {
            try {
                val id = localList[index].id
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
                id?.let {
                    val changeId = Random.nextInt()
                    lastChange.value = changeId
                    val response = apiService.checkItem(id)
                    delay(2000)
                    // If another check hasn't happened in the last 2 seconds, apply any changes brought down from the global version of the list
                    if (lastChange.value == changeId) {
                        apply(response)
                    }
                }
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to check item: ${e.message}")
            }
        }
    }

    fun editItem(index: Int,item: SmallShoppingItem) {
        scope.launch {
            try {
                localList[index] = localList[index].copy(name = item.name)
                localList[index].id?.let { id ->
                    val response = apiService.editItem(id, item)
                    apply(response)
                }
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to edit item: ${e.message}")
            }
        }
    }

    fun moveItem(fromIndex: Int, toIndex: Int) {
        scope.launch {
            try {
                val movingName = localList[fromIndex].name
                localList.add(toIndex, localList.removeAt(fromIndex))
                latestMove.value = Pair(movingName,toIndex)
                val changeId = Random.nextInt()
                lastChange.value = changeId
                delay(1000)
                // Only reorder the list globally if there is not a newer reorder request
                if (latestMove.value?.first == movingName && latestMove.value?.second == toIndex) {
                    val response = apiService.reorder(orderedList=localList)
                    // Only apply changes from the database if no new changes have been made
                    if (lastChange.value == changeId) {
                        apply(response)
                    }
                }

            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to move item: ${e.message}")

            }

        }
    }
}