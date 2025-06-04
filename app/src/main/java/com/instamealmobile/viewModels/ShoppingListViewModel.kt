package com.instamealmobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.ShoppingItem
import com.instamealmobile.data.SmallShoppingItem
import com.instamealmobile.network.ShoppingListService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class ShoppingListViewModel @Inject constructor(private val apiService: ShoppingListService): ViewModel() {
    private val _shoppingList = MutableLiveData<ApiState<List<ShoppingItem>>>(ApiState.Loading)
    val shoppingList: LiveData<ApiState<List<ShoppingItem>>> = _shoppingList
    val householdId = "3hPKx3PwkPkPPlCVs53q"

    fun fetchShoppingList() {
        viewModelScope.launch {
            try {
                val response = apiService.getShoppingList(householdId)
                _shoppingList.value = ApiState.Success(response)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun addItemToList(text: String) {
        viewModelScope.launch {
            try {
                _shoppingList.value = ApiState.Loading
                val response = apiService.postShoppingList(householdId, ShoppingItem(
                    name=text,
                    user_id = "OKmkTNVx4TR6D6u9BjMJ"))
                _shoppingList.value = ApiState.Success(response.updated_list)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to update shopping list: ${e.message}")
            }
        }
    }

    fun checkItem(index: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.checkItem(householdId, index)
                _shoppingList.value = ApiState.Success(response.updated_list)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to check item: ${e.message}")
            }
        }
    }

    fun editItem(index: Int,item: SmallShoppingItem) {
        viewModelScope.launch {
            try {
                val response = apiService.editItem(householdId, index, item)
                _shoppingList.value = ApiState.Success(response.updated_list)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to check item: ${e.message}")
            }
        }
    }
}