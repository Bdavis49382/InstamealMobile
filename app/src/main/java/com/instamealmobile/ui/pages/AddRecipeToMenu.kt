package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.notifications.AndroidAlarmScheduler
import com.instamealmobile.R
import com.instamealmobile.data.Recipe
import com.instamealmobile.ui.DatePickerModal
import com.instamealmobile.ui.DeleteButton
import com.instamealmobile.ui.SideButton
import com.instamealmobile.ui.SideButtons
import com.instamealmobile.ui.SmartAsyncImage
import com.instamealmobile.viewModels.MenuViewModel
import com.instamealmobile.viewModels.RemovedIngredient
import kotlinx.coroutines.launch

@Composable
fun AddRecipeToMenu(recipe : Recipe, confirm: () -> Unit) {
    val viewModel: MenuViewModel =  viewModel()
    val context = LocalContext.current
    val scheduler = AndroidAlarmScheduler(context.applicationContext)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.ingredients.clear()
        viewModel.ingredients.addAll(recipe.ingredients)
        viewModel.date = 0L
        viewModel.note = ""
    }
    if (viewModel.datePickerOpen) {
        DatePickerModal({viewModel.date = it ?: 0}) {viewModel.datePickerOpen = false }
    }

    Box(contentAlignment = Alignment.TopStart, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Row(modifier = Modifier) {
                Column(
                    modifier = Modifier.width(200.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = recipe.title,
                        style = TextStyle( fontSize = 30.sp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = recipe.src_name ?: "",
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Text(
                        text = if (viewModel.date != 0L) "Making on ${viewModel.getDateString(viewModel.date,"MM/dd/yyyy")}" else "",
                        fontStyle = FontStyle.Italic,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
                Box(modifier = Modifier) {
                    SmartAsyncImage(
                        url = recipe.img_link,
                        backupText = recipe.title
                    )

                }
            }
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = viewModel.note,
                    onValueChange = { viewModel.note = it },
                    placeholder = {Text("Add note (optional)")},
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 13.sp),
                    modifier = Modifier
                        .padding(start = 5.dp, top = 30.dp, end = 20.dp, bottom = 30.dp)
                        .width(200.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
                Button({viewModel.datePickerOpen = true}) {
                    Icon(painter = painterResource(R.drawable.baseline_edit_calendar_24), "Add Date")
                }
            }
            Text(
                text = "Items to be Added To Shopping List",
                style = TextStyle(fontSize = 25.sp),
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()) {
                Text("Already stocked up?", modifier = Modifier.padding(end = 10.dp))
                OutlinedButton({
                    val removedIngredients = viewModel.ingredients.toList()
                    viewModel.ingredients.clear()
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Removed ${removedIngredients.size} items from list",
                            actionLabel = "Undo"
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            if (removedIngredients.isNotEmpty()) {
                                viewModel.ingredients.addAll(removedIngredients)
                            }
                        }

                    }
                }) {
                    Text("Remove All")
                }
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                itemsIndexed(viewModel.ingredients) { index,item ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .width(250.dp)
                        )
                        DeleteButton(
                            onClick = {
                                viewModel.removedIngredients.add(RemovedIngredient(viewModel.ingredients[index],index))
                                viewModel.ingredients.removeAt(index)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Removed 1 item from list",
                                        actionLabel = "Undo"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        if (viewModel.removedIngredients.isNotEmpty()) {
                                            viewModel.removedIngredients.last().let { lastRemoved ->
                                                viewModel.ingredients.add(lastRemoved.index,lastRemoved.name)
                                            }
                                            viewModel.removedIngredients.removeAt(viewModel.removedIngredients.lastIndex)
                                        }
                                    }

                                }
                            }
                        )
                    }
                }
            }
        }
        SideButtons {
            SideButton({viewModel.addRecipe(recipe, scheduler)
                confirm()}
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Submit")
            }
        }
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(snackbarData = data)
            }
        }
    }
}
