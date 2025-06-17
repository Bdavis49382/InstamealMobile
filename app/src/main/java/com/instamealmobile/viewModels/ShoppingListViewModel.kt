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
        scope.launch {
            try {
                val response = apiService.getShoppingList()
                _shoppingList.value = ApiState.Success(response)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun addItemToList(text: String) {
        scope.launch {
            try {
                _shoppingList.value = ApiState.Loading
                val response = apiService.postShoppingList(ShoppingItem(
                    name=text))
                _shoppingList.value = ApiState.Success(response)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to update shopping list: ${e.message}")
            }
        }
    }

    fun checkItem(index: Int) {
        scope.launch {
            try {
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
                _shoppingList.value = ApiState.Success(response)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to check item: ${e.message}")
            }
        }
    }
}