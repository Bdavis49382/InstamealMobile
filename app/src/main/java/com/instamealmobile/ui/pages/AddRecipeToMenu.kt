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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.viewModels.AddRecipeToFeedViewModel
import com.instamealmobile.viewModels.AddRecipeToMenuViewModel
import com.instamealmobile.viewModels.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeToMenu(onDismiss : () -> Unit, confirm: () -> Unit, recipe : Recipe) {
    val sheetState = rememberModalBottomSheetState()
    val viewModel: AddRecipeToMenuViewModel =  viewModel()

    LaunchedEffect(Unit) {
        viewModel.ingredients.addAll(recipe.ingredients)
    }

    ModalBottomSheet(onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(),
        dragHandle = { BottomSheetDefaults.DragHandle()}
    ) {
        Box(contentAlignment = Alignment.TopStart, modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                Row(modifier = Modifier.height(100.dp)) {
                    Column(
                        modifier = Modifier.width(200.dp).fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = recipe.title,
                            style = TextStyle(color = Color.Black, fontSize = 30.sp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = recipe.src_name ?: "",
                            fontStyle = FontStyle.Italic,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                    Box(modifier = Modifier.width(400.dp)) {
                        AsyncImage(
                            model = recipe.img_link ?: "https://recipe-graphics.grocerywebsite.com/0_GraphicsRecipes/4589_4k.jpg",
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp)),
                            contentDescription = null
                        )

                    }
                }
                Text(
                    text = "Add a Note (optional)",
                    style = TextStyle(color = Color.Black, fontSize = 15.sp),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = viewModel.note,
                    onValueChange = { viewModel.note = it },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black, fontSize = 13.sp),
                    modifier = Modifier
                        .padding(start = 5.dp, top = 15.dp, end = 120.dp, bottom = 30.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                )
                Text(
                    text = "Will Add Following Items to Shopping List:",
                    style = TextStyle(color = Color.Black, fontSize = 15.sp),
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    itemsIndexed(viewModel.ingredients) { index,item ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = {viewModel.ingredients.removeAt(index)}, shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor =
                                        MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                ),
                                modifier = Modifier
                                    .size(60.dp)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Remove Item",
                                    modifier = Modifier
                                )
                            }
                            Text(
                                text = item,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                            )
                        }
                    }
                }
            }
            Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 100.dp, horizontal = 20.dp)
            ) {
                Button({viewModel.addRecipe(recipe)
                    confirm()}, shape = RoundedCornerShape(10.dp), modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 5.dp)
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Edit")
                }
            }
        }
    }
}
