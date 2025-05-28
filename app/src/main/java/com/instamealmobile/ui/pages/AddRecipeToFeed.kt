package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.ui.EditableText
import com.instamealmobile.viewModels.AddRecipeToFeedViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeToFeed(onDismiss : () -> Unit, confirm: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val viewModel: AddRecipeToFeedViewModel =  viewModel()


    ModalBottomSheet(onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(),
        dragHandle = { BottomSheetDefaults.DragHandle()}
    ) {
        Box(contentAlignment = Alignment.TopStart, modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .padding(20.dp)
            ) {
                Row(modifier = Modifier.height(150.dp)) {
                    Column(modifier = Modifier.width(200.dp).fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                        TextField(
                            value = viewModel.title,
                            placeholder = {Text("Title")},
                            onValueChange = {viewModel.title = it},
                            singleLine = true,
                            textStyle = TextStyle(color = Color.Black, fontSize = 13.sp),
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )
                        TextField(
                            value = viewModel.source,
                            placeholder = {Text("Source")},
                            onValueChange = {viewModel.source = it},
                            singleLine = true,
                            textStyle = TextStyle(color = Color.Black, fontSize = 12.sp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                        )
                    }
                    Box(modifier = Modifier.width(400.dp)) {
//                        Upload Image File
//                        AsyncImage(
//                            model = "https://placehold.co/200x200",
//                            modifier = Modifier
//                                .clip(RoundedCornerShape(10.dp)),
//                            contentDescription = null
//                        )

                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    item {
                        Text(
                            text = "Ingredients",
                            style = TextStyle(color = Color.Black, fontSize = 25.sp),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)
                        )

                    }
                    itemsIndexed(viewModel.ingredients) { index,item ->
                        Row {
                            EditableText(text = item, placeholder = "Ingredient", modifier = Modifier
                                .width(270.dp)
                                .padding(end = 5.dp, bottom = 5.dp)
                                .clip(RoundedCornerShape(20.dp))
                            ) {
                                viewModel.ingredients[index] = it
                            }
                            Button({viewModel.ingredients.removeAt(index)}, shape = CircleShape, modifier = Modifier.padding(horizontal = 5.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove")
                            }
                        }
                    }
                    item {
                        TextField(
                            value = viewModel.newIngredient,
                            placeholder = {Text("Ingredient")},
                            onValueChange = {viewModel.newIngredient = it},
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    viewModel.ingredients.add(viewModel.newIngredient)
                                    viewModel.newIngredient = ""
                                }
                            ),
                            singleLine = true,
                            textStyle = TextStyle(color = Color.Black, fontSize = 12.sp),
                            modifier = Modifier
                                .width(270.dp)
                                .padding(end = 5.dp, bottom = 5.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )

                    }
                    item {
                        Text(text="Steps",
                            style = TextStyle(color = Color.Black, fontSize = 25.sp),
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }
                    itemsIndexed(viewModel.steps) { index,item ->
                        Row {
                            EditableText(text = item, placeholder = "Step", modifier = Modifier
                                .width(270.dp)
                                .padding(end = 5.dp, bottom = 5.dp)
                                .clip(RoundedCornerShape(20.dp))
                            ) {
                                viewModel.steps[index] = it
                            }
                            Button({viewModel.steps.removeAt(index)}, shape = CircleShape) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove")
                            }
                        }
                    }
                    item {
                        TextField(
                            value = viewModel.newStep,
                            placeholder = {Text("Step")},
                            onValueChange = {viewModel.newStep = it},
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    viewModel.steps.add(viewModel.newStep)
                                    viewModel.newStep = ""
                                }
                            ),
                            singleLine = true,
                            textStyle = TextStyle(color = Color.Black, fontSize = 12.sp),
                            modifier = Modifier
                                .width(270.dp)
                                .padding(end = 5.dp, bottom = 5.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )

                    }
                }

            }
            Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 100.dp, horizontal = 20.dp)
            ) {
                Button({
                    viewModel.submitRecipe()
//                    confirm()
                       }, shape = RoundedCornerShape(10.dp), modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 5.dp)
                ) {
                    Text("Save")
                }
            }
        }
    }
}
