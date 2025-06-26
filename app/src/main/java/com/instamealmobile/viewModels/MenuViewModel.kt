package com.instamealmobile.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.MenuListItem
import com.instamealmobile.data.Recipe
import com.instamealmobile.network.MenuService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val apiService: MenuService): ViewModel() {
    private val _menu = MutableStateFlow<ApiState<List<MenuListItem>>>(ApiState.Loading)
    val menu: MutableStateFlow<ApiState<List<MenuListItem>>> = _menu
    private val _selected = MutableStateFlow<ApiState<MenuItem>>(ApiState.Loading)
    val selected: MutableStateFlow<ApiState<MenuItem>> = _selected
    var scope = viewModelScope
    var isRefreshing by mutableStateOf(false)

    // Variables for adding a new item to menu
    var ingredients = mutableStateListOf<String>()
    var note by mutableStateOf("")
    var date by mutableLongStateOf(0L)
    var datePickerOpen by mutableStateOf(false)

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
        scope.launch {
            try {
                val response = apiService.addRecipe(MenuItem(
                    note = note,
                    date = getLocalDate(),
                    active_items = ingredients,
                    title = recipe.title,
                    recipe = recipe,
                    recipe_id = recipe.id,
                    img_link = recipe.img_link,
                ))
                response.forEachIndexed {index, value -> value.index  = index}
                _menu.value = ApiState.Success(response)
            } catch (e: Exception) {
                _menu.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun updateMenuItem(index: Int,menuItem: MenuItem) {
        scope.launch {
            try {
                val response = apiService.updateRecipeByIndex(index, menuItem)
                _selected.value = ApiState.Success(response)
            } catch (e: Exception) {
                _menu.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun getMenu() {
        scope.launch {
            try {
                _menu.value = ApiState.Loading
                val response = apiService.getMenu()
                response.forEachIndexed {index, value -> value.index  = index}
                _menu.value = ApiState.Success(response)
            } catch (e: Exception) {
                _menu.value = ApiState.Error("Failed to fetch data: ${e.message}")
            } finally {
                isRefreshing = false

            }
        }
    }

    fun refreshMenu() {
        isRefreshing = true
        getMenu()
    }

    fun finishMeal(recipeId: String, rating: Float?) {
        scope.launch {
            try {
                _menu.value = ApiState.Loading
                val response = apiService.finishMeal(recipeId,rating)
                response.forEachIndexed {index, value -> value.index  = index}
                _menu.value = ApiState.Success(response)
            } catch (e: Exception) {
                _menu.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }

    }

    fun getRecipe(index: Int) {
        _selected.value = ApiState.Loading
        scope.launch {
            try {
                val response = apiService.getRecipeByIndex(index)
                _selected.value = ApiState.Success(response)
            } catch (e: Exception) {
                _selected.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }
}
