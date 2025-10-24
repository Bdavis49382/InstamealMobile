package com.instamealmobile.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.data.RecipeIdentifier
import com.instamealmobile.network.MenuService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val apiService: MenuService): ViewModel() {
    private val _recipe = MutableStateFlow<ApiState<Recipe>>(ApiState.Loading)
    val recipe: MutableStateFlow<ApiState<Recipe>> = _recipe
    var scope = viewModelScope

    fun getRecipe(recipe: RecipeIdentifier?) {
        _recipe.value = ApiState.Loading
        scope.launch {
            try {

                val response = when (recipe) {
                    is RecipeIdentifier.RecipeId -> apiService.getRecipe(recipe.id)
                    is RecipeIdentifier.RecipeLink -> apiService.getRecipeOnline(recipe.link)
                    is RecipeIdentifier.FullRecipe -> recipe.recipe
                    else -> throw Exception("Cannot get a recipe when one has not been selected.")
                }
                _recipe.value = ApiState.Success(response)
            } catch (e: HttpException) {
                if (e.code() == 400) {
                    _recipe.value = ApiState.Error("There is no recipe here, the website serving this recipe must have changed its mind. Try another recipe.")
                } else {
                    _recipe.value = ApiState.Error("Failed to fetch data: ${e.message}")
                }
            }
            catch (e: Exception) {
                _recipe.value = ApiState.Error("Failed to fetch data: ${e.message}")
            }
        }
    }


}
