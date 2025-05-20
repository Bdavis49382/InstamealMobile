package com.instamealmobile.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.network.FeedService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeToFeedViewModel @Inject constructor(private val apiService: FeedService): ViewModel() {
    var ingredients = mutableStateListOf<String>()
    var title by  mutableStateOf("")
    var source by mutableStateOf("")
    var newIngredient by mutableStateOf("")
    var newStep by mutableStateOf("")
    var img_link by mutableStateOf("")
    var steps = mutableStateListOf<String>()
    val householdId = "3hPKx3PwkPkPPlCVs53q"


    fun submitRecipe() {
        viewModelScope.launch {
            try {
                val response = apiService.addRecipe(householdId, "OKmkTNVx4TR6D6u9BjMJ", Recipe(
                    ingredients = ingredients,
                    title = title,
                    src_name = source,
                    img_link = img_link,
                    instructions = steps
                ))
            } catch (e: Exception) {
                // TODO: Add logging so something can be logged here
            }
        }

    }

}