package com.instamealmobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.network.MenuService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val apiService: MenuService): ViewModel() {
    private val _recipe = MutableLiveData<ApiState<Recipe>>(ApiState.Loading)
    val recipe: LiveData<ApiState<Recipe>> = _recipe
    val householdId = "3hPKx3PwkPkPPlCVs53q"

    fun getRecipe(recipe: Recipe) {
        _recipe.value = ApiState.Loading
        viewModelScope.launch {
            try {

                val response = if (recipe.id.isNullOrEmpty()) {
                    apiService.getRecipeOnline(householdId, recipe.src_link ?: "")
                } else  {
                    apiService.getRecipe(householdId, recipe.id)
                }
                _recipe.value = ApiState.Success(response)
            } catch (e: Exception) {
                _recipe.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }


}
