package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.ui.RecipeView
import com.instamealmobile.ui.SideButton
import com.instamealmobile.ui.placeholders.RecipeViewPlaceholder
import com.instamealmobile.viewModels.RecipeViewModel

@Composable
fun PreviewRecipe(lazyListState: LazyListState,closeSheet : () -> Unit, recipe: Recipe,  confirm: (Recipe) -> Unit) {
    val viewModel: RecipeViewModel =  viewModel()
    val recipeState by viewModel.recipe.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getRecipe(recipe)
    }
    Box(contentAlignment = Alignment.TopStart, modifier = Modifier.fillMaxSize()) {
        when (recipeState) {
            is ApiState.Loading -> {
                RecipeViewPlaceholder()
            }

            is ApiState.Success<*> -> {
                val recipeData = (recipeState as ApiState.Success<Recipe>).data
                RecipeView(lazyListState, recipeData)
                Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 200.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        SideButton(
                            {confirm(recipeData)}, modifier = Modifier
                                .padding(vertical = 5.dp)
                        ) {
                            Text("Add to Menu")
                        }
                        SideButton(
                            closeSheet, modifier = Modifier
                                .padding(vertical = 5.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
            }

            is ApiState.Error -> {
                val error = (recipeState as ApiState.Error).message
                Text(error)
            }

            null -> TODO()
        }
    }
}
