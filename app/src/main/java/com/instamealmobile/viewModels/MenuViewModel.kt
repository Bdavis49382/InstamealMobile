package com.instamealmobile.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.Recipe
import com.instamealmobile.network.MenuService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val apiService: MenuService): ViewModel() {
    private val _menu = MutableLiveData<ApiState<List<MenuItem>>>(ApiState.Loading)
    val menu: LiveData<ApiState<List<MenuItem>>> = _menu
    private val _selected = MutableLiveData<ApiState<MenuItem>>(ApiState.Loading)
    val selected: LiveData<ApiState<MenuItem>> = _selected

    // Variables for adding a new item to menu
    var ingredients = mutableStateListOf<String>()
    var note by mutableStateOf("")
    var date by mutableLongStateOf(0L)
    var datePickerOpen by mutableStateOf(false)
    val householdId = "3hPKx3PwkPkPPlCVs53q"

    val offset = ZoneId.systemDefault().rules.getOffset(Instant.ofEpochMilli(date))

    fun getDateString(date: Long, format: String): String {
        return if (date != 0L)
            SimpleDateFormat(format, Locale.getDefault()).format(date - offset.totalSeconds * 1000L)
        else ""

    }
    fun getLocalDate(): Date? {
        return if (date != 0L)
            Date(date - offset.totalSeconds*1000L)
        else null
    }

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            try {
                val response = apiService.addRecipe(householdId, "OKmkTNVx4TR6D6u9BjMJ", MenuItem(
                    note = note,
                    date = getLocalDate(),
                    active_items = ingredients,
                    title = recipe.title,
                    recipe = recipe,
                    recipe_id = recipe.id,
                    img_link = recipe.img_link,
                ))
                _menu.value = ApiState.Success(response)
            } catch (e: Exception) {
                _menu.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun getMenu() {
        viewModelScope.launch {
            try {
                _menu.value = ApiState.Loading
                val response = apiService.getMenu(householdId)
                _menu.value = ApiState.Success(response)
            } catch (e: Exception) {
                _menu.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }
    fun finishMeal(recipeId: String, rating: Float?) {
        viewModelScope.launch {
            try {

                val response = apiService.finishMeal(householdId, recipeId,"OKmkTNVx4TR6D6u9BjMJ", rating)
                _menu.value = ApiState.Success(response)
            } catch (e: Exception) {
                _menu.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }

    }

    fun getRecipe(index: Int) {
        _selected.value = ApiState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.getRecipeByIndex(householdId,index)
                _selected.value = ApiState.Success(response)
            } catch (e: Exception) {
                _selected.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }

    }
}
