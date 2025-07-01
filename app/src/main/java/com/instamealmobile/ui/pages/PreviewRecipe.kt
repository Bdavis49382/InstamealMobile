package com.instamealmobile.ui.pages

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.OpenSheet
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.data.RecipeIdentifier
import com.instamealmobile.ui.RecipeView
import com.instamealmobile.ui.SideButton
import com.instamealmobile.ui.SideButtons
import com.instamealmobile.ui.placeholders.RecipeViewPlaceholder
import com.instamealmobile.viewModels.NavViewModel
import com.instamealmobile.viewModels.Purpose
import com.instamealmobile.viewModels.RecipeViewModel

@Composable
fun PreviewRecipe(
    lazyListState: LazyListState
) {
    val viewModel: RecipeViewModel =  viewModel()
    val nav: NavViewModel = viewModel()
    val recipeState by viewModel.recipe.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getRecipe(nav.pickedRecipe)
    }
    Box(contentAlignment = Alignment.TopStart, modifier = Modifier.fillMaxSize()) {
        Crossfade(targetState = recipeState, label = "ContentSwitch") { screenState ->
            when (screenState) {
                is ApiState.Loading -> {
                    RecipeViewPlaceholder()
                }

                is ApiState.Success<*> -> if (recipeState is ApiState.Success) {
                    val recipeData = (recipeState as ApiState.Success<Recipe>).data
                    RecipeView(lazyListState, recipeData)
                    SideButtons {
                        SideButton(
                            {
                                nav.navigateTo(OpenSheet.AddRecipeToMenu,
                                    recipe = RecipeIdentifier.factory(recipeData))
                            }
                        ) {
                            Text("Add to Menu")
                        }
                        if (!recipeData.id.isNullOrEmpty()) {
                            SideButton(
                                {
                                    nav.navigateTo(OpenSheet.AddRecipeToFeed,
                                        recipe = RecipeIdentifier.factory(recipeData),
                                        purpose = Purpose.FeedEdit)
                                }
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            }

                        }
                    }
                }

                is ApiState.Error -> if (recipeState is ApiState.Error) {
                    val error = (recipeState as ApiState.Error).message
                    Text(error)
                }

                else -> {}
            }
        }
    }
}
