package com.instamealmobile.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.notifications.AlarmItem
import com.instamealmobile.notifications.AndroidAlarmScheduler
import com.instamealmobile.R
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.MenuListItem
import com.instamealmobile.data.Recipe
import com.instamealmobile.network.MenuService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class RemovedIngredient(val name: String, val index: Int)

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
    var removedIngredients = mutableStateListOf<RemovedIngredient>()
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

    fun addRecipe(recipe: Recipe, scheduler: AndroidAlarmScheduler) {
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
                // If a date was provided, set an alert for a reminder notification.
                getLocalDate()?.let {
                    val alarmTime = LocalDateTime.of(
                        it.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        LocalTime.of(17,0))
                    scheduler.schedule(
                        AlarmItem(
                            alarmTime,
                            recipe.title,
                            recipe.id.toString(),
                            R.drawable.baseline_calendar_today_24)
                    )

                }
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
                getMenu(allowCache = false)
            } catch (e: Exception) {
                _menu.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun getMenu(allowCache: Boolean = true) {
        scope.launch {
            try {
                _menu.value = ApiState.Loading
                val response = if (allowCache) apiService.getMenu() else apiService.getMenu(allowCache="no-cache")
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

    fun removeMenuItem(recipeId: String) {
        scope.launch {
            try {
                _menu.value = ApiState.Loading
                val response = apiService.deleteMenuItem(recipeId)
                response.forEachIndexed {index, value -> value.index  = index}
                _menu.value = ApiState.Success(response)
            } catch (e: Exception) {
                _menu.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }

    fun getRecipe(recipeId: String, allowCache: Boolean = true) {
        _selected.value = ApiState.Loading
        scope.launch {
            try {
                val response = if (allowCache) apiService.getRecipeById(recipeId) else apiService.getRecipeById(recipeId, allowCache = "no-cache")
                _selected.value = ApiState.Success(response)
            } catch (e: HttpException) {
                if (e.code() == 400) {
                    _selected.value = ApiState.Error("There is no recipe here, you may have been kicked or the menu has been changed very recently. Try refreshing.")
                } else {
                    _selected.value = ApiState.Error("Failed to fetch data: ${e.message}")
                }
            }
            catch (e: Exception) {
                _selected.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }
}
