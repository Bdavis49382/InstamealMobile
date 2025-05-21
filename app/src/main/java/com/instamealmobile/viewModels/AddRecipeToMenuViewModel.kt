package com.instamealmobile.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.Recipe
import com.instamealmobile.network.FeedService
import com.instamealmobile.network.MenuService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeToMenuViewModel @Inject constructor(private val apiService: MenuService): ViewModel() {
    var ingredients = mutableStateListOf<String>()
    var note by mutableStateOf("")
//    var date by mutableStateOf("")
    val householdId = "3hPKx3PwkPkPPlCVs53q"

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            try {
                val response = apiService.addRecipe(householdId, "OKmkTNVx4TR6D6u9BjMJ", MenuItem(
                    note = note,
//                    date = date,
                    active_items = ingredients,
                    title = recipe.title,
                    recipe = recipe,
                    recipe_id = recipe.id,
                    img_link = recipe.img_link,
                ))
            } catch (e: Exception) {
                // TODO: Add logging so something can be logged here
            }
        }

    }

}