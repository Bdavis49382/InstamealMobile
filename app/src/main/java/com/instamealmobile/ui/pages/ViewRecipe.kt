package com.instamealmobile.ui.pages

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.Recipe
import com.instamealmobile.data.ScreenState
import com.instamealmobile.ui.RecipeView
import com.instamealmobile.ui.SideButton
import com.instamealmobile.ui.placeholders.RecipeViewPlaceholder
import com.instamealmobile.viewModels.MenuViewModel
import java.text.SimpleDateFormat

@Composable
fun ViewRecipe(lazyListState: LazyListState,recipe: Recipe, confirm: () -> Unit) {
    val menuViewModel: MenuViewModel = viewModel()
    val menuItemState by menuViewModel.selected.collectAsState()

    LaunchedEffect(Unit) {
        menuViewModel.getRecipe(recipe.index)
    }
    val screenState = when (menuItemState) {
        is ApiState.Loading -> ScreenState.Loading
        is ApiState.Success -> ScreenState.Success
        is ApiState.Resting -> ScreenState.Resting
        is ApiState.Error -> ScreenState.Error
    }

    Box(contentAlignment = Alignment.TopStart, modifier = Modifier.fillMaxSize()) {
        Crossfade(targetState = screenState, label = "ContentSwitch") { screenState ->
            when (screenState) {
                ScreenState.Loading -> {
                    RecipeViewPlaceholder()
                }
                ScreenState.Success -> {
                    if (menuItemState is ApiState.Success) {
                        val menuItem = (menuItemState as ApiState.Success<MenuItem>).data
                        Column {
                            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                                if (menuItem.note.isNotEmpty()) {
                                    Text(menuItem.note)
                                }
                                if (menuItem.date != null) {
                                    Text(SimpleDateFormat("E, MMMM dd").format(menuItem.date))
                                }

                            }
                            RecipeView(lazyListState, menuItem.recipe ?: Recipe(title = ""))
                        }
                        Box(
                            contentAlignment = Alignment.BottomEnd, modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 200.dp)
                        ) {
                            SideButton({
                                confirm()
                            }
                            ) {
                                Text("Finish")
                            }
                        }
                    }
                }

                ScreenState.Error -> {
                    val error = (menuItemState as ApiState.Error).message
                    Text(error)
                }

                else -> {}
            }
        }
    }
}
