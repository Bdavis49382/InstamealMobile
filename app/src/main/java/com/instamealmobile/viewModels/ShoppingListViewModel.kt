package com.instamealmobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
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
    val user = Firebase.auth.currentUser

    fun fetchShoppingList() {
        viewModelScope.launch {
            try {
                val response = apiService.getShoppingList()
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
                val response = apiService.postShoppingList(ShoppingItem(
                    name=text,
                    user_id = user?.uid?:""))
                _shoppingList.value = ApiState.Success(response)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to update shopping list: ${e.message}")
            }
        }
    }

    fun checkItem(index: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.checkItem(index)
                _shoppingList.value = ApiState.Success(response)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to check item: ${e.message}")
            }
        }
    }

    fun editItem(index: Int,item: SmallShoppingItem) {
        viewModelScope.launch {
            try {
                val response = apiService.editItem(index, item)
                _shoppingList.value = ApiState.Success(response)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to check item: ${e.message}")
            }
        }
    }
}