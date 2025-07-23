package com.instamealmobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.ShoppingItem
import com.instamealmobile.data.SmallShoppingItem
import com.instamealmobile.network.ShoppingListService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class ShoppingListViewModel @Inject constructor(private val apiService: ShoppingListService): ViewModel() {
    private val _shoppingList = MutableStateFlow<ApiState<List<ShoppingItem>>>(ApiState.Loading)
    val shoppingList: MutableStateFlow<ApiState<List<ShoppingItem>>> = _shoppingList
    var scope = viewModelScope

    fun fetchShoppingList() {
        _shoppingList.value = ApiState.Loading
        scope.launch {
            try {
                val response = apiService.getShoppingList()
                response.forEachIndexed {index, value -> value.index  = index}
                _shoppingList.value = ApiState.Success(response)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun addItemToList(text: String, userInitial: String, scrollToTop: () -> Unit) {
        scope.launch {
            try {
                if (shoppingList.value is ApiState.Success) {
                    val items = (shoppingList.value as ApiState.Success).data.toMutableList()
                    items.add(ShoppingItem(name=text, index = items.size, user_initial = userInitial))
                    _shoppingList.value = ApiState.Success(items)
                    scrollToTop()
                }
                val response = apiService.postShoppingList(ShoppingItem(
                    name=text))
                response.forEachIndexed {index, value -> value.index  = index}
                apply(response, scrollToTop)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to update shopping list: ${e.message}")
            }
        }
    }
    fun apply(newList: List<ShoppingItem>, scrollToTop: () -> Unit) {
        //    If there are any differences, apply the newList, otherwise leave it
        if (shoppingList.value is ApiState.Success) {
            for (item in (shoppingList.value as ApiState.Success).data.zip(newList)) {
                if (item.first.name != item.second.name) {
                    _shoppingList.value = ApiState.Success(newList)
                    scrollToTop()
                    return
                }
            }
        } else {
            _shoppingList.value = ApiState.Success(newList)
            scrollToTop()
        }
    }

    fun checkItem(index: Int) {
        scope.launch {
            try {
                if (shoppingList.value is ApiState.Success) {
                    val items = (shoppingList.value as ApiState.Success).data.toMutableList()
                    items[index] = items[index].copy(checked = !items[index].checked)
                    _shoppingList.value = ApiState.Success(items)
                }
                apiService.checkItem(index)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to check item: ${e.message}")
            }
        }
    }

    fun editItem(index: Int,item: SmallShoppingItem) {
        scope.launch {
            try {
                val response = apiService.editItem(index, item)
                _shoppingList.value = ApiState.Success(response)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to check item: ${e.message}")
            }
        }
    }
}