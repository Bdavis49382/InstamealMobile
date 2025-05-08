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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.FontScaling
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.instamealmobile.data.ApiState
import com.instamealmobile.makeLongList
import com.instamealmobile.ui.CheckItem
import com.instamealmobile.viewModels.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewRecipe(onDismiss : () -> Unit, confirm: () -> Unit, recipe : String) {
    val sheetState = rememberModalBottomSheetState()
    var newItemText by remember { mutableStateOf("") }
    val viewModel: ShoppingListViewModel =  viewModel()
    val shoppingListState by viewModel.shoppingList.observeAsState()
    val height = 350.dp

    LaunchedEffect(Unit) {
        viewModel.fetchShoppingList()
    }

    ModalBottomSheet(onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(),
        dragHandle = { BottomSheetDefaults.DragHandle()}
    ) {
        Box(contentAlignment = Alignment.CenterEnd) {
            Column(modifier = Modifier
                .padding(20.dp)
            ) {
                Row(modifier = Modifier.height(100.dp)) {
                    Column(modifier = Modifier.width(200.dp).fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                        Text(
                            text = recipe,
                            style = TextStyle(color = Color.Black, fontSize = 30.sp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(text = "From AllRecipes.com",
                            fontStyle = FontStyle.Italic,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp))
                    }
                    Box(modifier = Modifier.width(400.dp)) {
                        AsyncImage(
                            model = "https://recipe-graphics.grocerywebsite.com/0_GraphicsRecipes/4589_4k.jpg",
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp)),
                            contentDescription = null
                        )

                    }
                }
                when (shoppingListState) {
                    is ApiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is ApiState.Success<*> -> {
                        val shoppingList = (shoppingListState as ApiState.Success<List<String>>).data
                        LazyColumn(
                            modifier = Modifier
                                .height(height)
                                .fillMaxWidth()
                        ) {
                            item {
                                Text(
                                    text = "Ingredients",
                                    style = TextStyle(color = Color.Black, fontSize = 25.sp),
                                    modifier = Modifier.fillMaxWidth()
                                )

                            }
                            items(shoppingList) { item ->
                                Text(text = item,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                )
                            }
                            item {
                                Text(text="Steps",
                                    style = TextStyle(color = Color.Black, fontSize = 25.sp),
                                    modifier = Modifier.padding(top = 5.dp)
                                    )
                            }
                            items(makeLongList(10)) { item ->
                                Text(text = item,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                )
                            }
                        }
                    }
                    is ApiState.Error -> {
                        val error = (shoppingListState as ApiState.Error).message
                        Text(error)
                    }

                    null -> TODO()
                }
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Button(confirm, shape = RoundedCornerShape(10.dp), modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 5.dp)
                ) {
                    Text("Add to Menu")
                }
                Button(onDismiss, shape = RoundedCornerShape(10.dp), modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 5.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }

            }
        }
    }
}
