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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.notifications.AlarmItem
import com.instamealmobile.notifications.AndroidAlarmScheduler
import com.instamealmobile.OpenAlert
import com.instamealmobile.OpenSheet
import com.instamealmobile.R
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.Recipe
import com.instamealmobile.data.RecipeIdentifier
import com.instamealmobile.data.RecipeIdentifier.RecipeId
import com.instamealmobile.data.ScreenState
import com.instamealmobile.ui.DatePickerModal
import com.instamealmobile.ui.EditableText
import com.instamealmobile.ui.RecipeView
import com.instamealmobile.ui.SideButton
import com.instamealmobile.ui.SideButtons
import com.instamealmobile.ui.placeholders.RecipeViewPlaceholder
import com.instamealmobile.viewModels.MenuViewModel
import com.instamealmobile.viewModels.NavViewModel
import com.instamealmobile.viewModels.Purpose
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Locale

@Composable
fun ViewRecipe(lazyListState: LazyListState, recipeIdentifier: RecipeIdentifier?, keepScreenOn: (Boolean) -> Unit) {
    val nav: NavViewModel = viewModel()
    val menuViewModel: MenuViewModel = viewModel()
    val menuItemState by menuViewModel.selected.collectAsState()
    var recipeIndex by remember { mutableIntStateOf(0) }
    var screenStaysOn by remember { mutableStateOf(false)}
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (recipeIdentifier is RecipeId) {
            menuViewModel.getRecipe(recipeIdentifier.id)
        } else if (recipeIdentifier is RecipeIdentifier.FullRecipe) {
            menuViewModel.getRecipe(recipeIdentifier.recipe.id.toString(), allowCache = false)
        } else {
            throw Exception("Tried to view a recipe without using the menu index. was ${recipeIdentifier?.javaClass.toString()}")
        }
    }
    val screenState = when (menuItemState) {
        is ApiState.Loading -> ScreenState.Loading
        is ApiState.Success -> ScreenState.Success
        is ApiState.Resting -> ScreenState.Resting
        is ApiState.Error -> ScreenState.Error
    }
    val scheduler = AndroidAlarmScheduler(context.applicationContext)
    if (menuViewModel.datePickerOpen) {
        DatePickerModal({
            if (menuItemState is ApiState.Success) {
                val menuItem = (menuItemState as ApiState.Success<MenuItem>).data
                menuViewModel.date = it ?: 0
                menuItem.date = menuViewModel.getLocalDate()
                menuViewModel.updateMenuItem(recipeIndex, menuItem)
                Toast.makeText(context, "Date Updated", Toast.LENGTH_SHORT).show()
                val alarmTime = LocalDateTime.of(
                    menuItem.date?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate(),
                    LocalTime.of(11,18))
                scheduler.schedule(
                    AlarmItem(
                        alarmTime,
                        menuItem.recipe?.title ?: "",
                        menuItem.recipe_id.toString(),
                        R.drawable.baseline_calendar_today_24)
                )
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
                    RecipeView(lazyListState, menuItem.recipe ?: Recipe(title = "")) {
                        Column(modifier = Modifier.padding(horizontal = 5.dp)) {
                            EditableText(text=menuItem.note,
                                precursor = "Note: ",
                                maxLines = 1,
                                placeholder = "Enter Note (Optional)") {
                                menuItem.note = it
                                menuViewModel.updateMenuItem(recipeIndex, menuItem)
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
                            Text("Keep Screen On Mode", modifier = Modifier.padding(top = 15.dp))
                            Switch(
                                checked = screenStaysOn,
                                onCheckedChange = {
                                    screenStaysOn = it
                                    keepScreenOn(screenStaysOn)}
                            )
                        }
                    }
                    SideButtons {
                        SideButton({
                            if (menuItem.recipe_id != null) {
                                nav.pickedRecipe = RecipeId(menuItem.recipe_id)
                                nav.navigateTo(OpenAlert.Rating)
                                nav.closeSheet()
                            } else {
                                throw Exception("Not enough information to finish recipe. No id was stored with menu item.")
                            }
                        }) {
                            Text("Finish")
                        }
                        SideButton(
                            {
                                menuItem.recipe?.index = recipeIndex
                                nav.navigateTo(OpenSheet.AddRecipeToFeed, RecipeIdentifier.factory(menuItem.recipe), Purpose.MenuEdit)
                            }
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        SideButton({
                            if (menuItem.recipe_id != null) {
                                nav.pickedRecipe = RecipeId(menuItem.recipe_id)
                                nav.navigateTo(OpenAlert.RemoveMeal)
                                nav.closeSheet()
                            } else {
                                throw Exception("Not enough information to remove recipe. No id was stored with menu item.")
                            }
                        }) {
                            Text("Remove")
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
