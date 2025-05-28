package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.Recipe
import com.instamealmobile.ui.RecipeView
import com.instamealmobile.viewModels.MenuViewModel
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewRecipe(onDismiss : () -> Unit, confirm: () -> Unit, recipe : Recipe) {
    val sheetState = rememberModalBottomSheetState()
    val menuViewModel: MenuViewModel = viewModel()
    val menuItemState by menuViewModel.selected.observeAsState()

    LaunchedEffect(Unit) {
        menuViewModel.getRecipe(recipe.index)
    }

    ModalBottomSheet(onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(),
        dragHandle = { BottomSheetDefaults.DragHandle()}
    ) {
        Box(contentAlignment = Alignment.TopStart, modifier = Modifier.fillMaxSize()) {
            when (menuItemState) {
                is ApiState.Loading -> {
                    CircularProgressIndicator()
                }
                is ApiState.Success<*> -> {
                    val menuItem = (menuItemState as ApiState.Success<MenuItem>).data
                    Column {
                        if (menuItem.note.isNotEmpty()) {
                            Text("Note: ${menuItem.note}")
                        }
                        if (menuItem.date != null) {
                            Text("Date: ${SimpleDateFormat("E, MMMM dd").format(menuItem.date)}")
                        }
                        RecipeView(menuItem.recipe?: Recipe(title=""))
                    }
                    Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 100.dp, horizontal = 20.dp)
                    ) {
                        Button({ menuViewModel.finishMeal(menuItem.recipe?.id ?: "",4.5f)
                            confirm()}, shape = RoundedCornerShape(10.dp), modifier = Modifier
                            .padding(horizontal = 30.dp, vertical = 5.dp)
                        ) {
                            Text("Finish")
                        }
                    }
                }
                is ApiState.Error -> {
                    val error = (menuItemState as ApiState.Error).message
                    Text(error)
                }

                null -> TODO()
            }
        }
    }
}
