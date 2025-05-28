package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.instamealmobile.viewModels.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewRecipe(onDismiss : () -> Unit, confirm: (Recipe) -> Unit, recipe : Recipe) {
    val sheetState = rememberModalBottomSheetState()
    val viewModel: RecipeViewModel =  viewModel()
    val recipeState by viewModel.recipe.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getRecipe(recipe)
    }

    ModalBottomSheet(onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(),
        dragHandle = { BottomSheetDefaults.DragHandle()}
    ) {
        Box(contentAlignment = Alignment.TopStart, modifier = Modifier.fillMaxSize()) {
            when (recipeState) {
                is ApiState.Loading -> {
                    CircularProgressIndicator()
                }

                is ApiState.Success<*> -> {
                    val recipeData = (recipeState as ApiState.Success<Recipe>).data
                    RecipeView(recipeData)
                    Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 100.dp, horizontal = 20.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Button(
                                {confirm(recipeData)}, shape = RoundedCornerShape(10.dp), modifier = Modifier
                                    .padding(horizontal = 30.dp, vertical = 5.dp)
                            ) {
                                Text("Add to Menu")
                            }
                            Button(
                                onDismiss, shape = RoundedCornerShape(10.dp), modifier = Modifier
                                    .padding(horizontal = 30.dp, vertical = 5.dp)
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
}
