package com.instamealmobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.Recipe
import com.instamealmobile.network.FeedService
import com.instamealmobile.network.MenuService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val apiService: MenuService): ViewModel() {
    private val _menu = MutableLiveData<ApiState<List<MenuItem>>>(ApiState.Loading)
    val menu: LiveData<ApiState<List<MenuItem>>> = _menu
    val householdId = "3hPKx3PwkPkPPlCVs53q"

    fun getMenu() {
        viewModelScope.launch {
            try {
                val response = apiService.getMenu(householdId)
                _menu.value = ApiState.Success(response)
            } catch (e: Exception) {
                _menu.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }
    fun finishMeal(recipeId: String, rating: Float) {
        viewModelScope.launch {
            try {

                val response = apiService.finishMeal(householdId, recipeId,"OKmkTNVx4TR6D6u9BjMJ", rating)
                _menu.value = ApiState.Success(response)
            } catch (e: Exception) {
                _menu.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }

    }
}
