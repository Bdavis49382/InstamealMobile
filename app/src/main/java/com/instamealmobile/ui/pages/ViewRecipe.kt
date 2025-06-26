package com.instamealmobile.ui.pages

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.R
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.Recipe
import com.instamealmobile.data.ScreenState
import com.instamealmobile.ui.DatePickerModal
import com.instamealmobile.ui.EditableText
import com.instamealmobile.ui.RecipeView
import com.instamealmobile.ui.SideButton
import com.instamealmobile.ui.SideButtons
import com.instamealmobile.ui.placeholders.RecipeViewPlaceholder
import com.instamealmobile.viewModels.MenuViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ViewRecipe(lazyListState: LazyListState,recipe: Recipe, editRecipe: (Recipe) -> Unit, confirm: () -> Unit) {
    val menuViewModel: MenuViewModel = viewModel()
    val menuItemState by menuViewModel.selected.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        menuViewModel.getRecipe(recipe.index)
    }
    val screenState = when (menuItemState) {
        is ApiState.Loading -> ScreenState.Loading
        is ApiState.Success -> ScreenState.Success
        is ApiState.Resting -> ScreenState.Resting
        is ApiState.Error -> ScreenState.Error
    }
    if (menuViewModel.datePickerOpen) {
        DatePickerModal({
            if (menuItemState is ApiState.Success) {
                val menuItem = (menuItemState as ApiState.Success<MenuItem>).data
                menuViewModel.date = it ?: 0
                menuItem.date = menuViewModel.getLocalDate()
                menuViewModel.updateMenuItem(recipe.index, menuItem)
                Toast.makeText(context, "Date Updated", Toast.LENGTH_SHORT).show()
            }
        }) {menuViewModel.datePickerOpen = false }
    }

    Box(contentAlignment = Alignment.TopStart, modifier = Modifier.fillMaxSize()) {
        Crossfade(targetState = screenState, label = "ContentSwitch") { screenState ->
            when (screenState) {
                ScreenState.Loading -> {
                    RecipeViewPlaceholder()
                }
                ScreenState.Success -> if (menuItemState is ApiState.Success){
                    val menuItem = (menuItemState as ApiState.Success<MenuItem>).data
                    Column {
                        RecipeView(lazyListState, menuItem.recipe ?: Recipe(title = "")) {
                            Column(modifier = Modifier.padding(horizontal = 5.dp)) {
                                EditableText(text=menuItem.note,
                                    precursor = "Note: ",
                                    maxLines = 1,
                                    placeholder = "Enter Note (Optional)") {
                                    menuItem.note = it
                                    menuViewModel.updateMenuItem(recipe.index, menuItem)
                                    Toast.makeText(context, "Menu Entry Note Updated", Toast.LENGTH_SHORT).show()
                                }
                                Row(verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                    if (menuItem.date != null) {
                                        Text(SimpleDateFormat("E, MMMM dd", Locale.getDefault()).format(menuItem.date!!))
                                    }
                                    Button({menuViewModel.datePickerOpen = true}) {
                                        Icon(painter = painterResource(R.drawable.baseline_edit_calendar_24), "Add Date")
                                    }
                                }
                            }
                        }
                    }
                    SideButtons {
                        SideButton({
                            confirm()
                        }) {
                            Text("Finish")
                        }
                        SideButton(
                            {
                                menuItem.recipe?.index = recipe.index
                                editRecipe(menuItem.recipe?:Recipe(title=""))
                            }
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }

                ScreenState.Error -> if (menuItemState is ApiState.Error) {
                    val error = (menuItemState as ApiState.Error).message
                    Text(error)
                }

                else -> {}
            }
        }
    }
}
