package com.instamealmobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.network.BackendApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ApiState<out T> {
    data object Loading : ApiState<Nothing>()
    data class Success<T>(val data: T) : ApiState<T>()
    data class Error(val message: String) : ApiState<Nothing>()
}


@HiltViewModel
class ShoppingListViewModel @Inject constructor(private val apiService: BackendApiService): ViewModel() {
    private val _shoppingList = MutableLiveData<ApiState<List<String>>>(ApiState.Loading)
    val shoppingList: LiveData<ApiState<List<String>>> = _shoppingList

    fun fetchShoppingList() {
        viewModelScope.launch {
            try {
                val response = apiService.getShoppingList("2FYwuLfa7WCCQcg9Wk7x")
                _shoppingList.value = ApiState.Success(response)
            } catch (e: Exception) {
                _shoppingList.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }
}