package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.instamealmobile.data.ApiState
import com.instamealmobile.makeLongList
import com.instamealmobile.viewModels.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeToFeed(onDismiss : () -> Unit, confirm: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    var titleText by remember { mutableStateOf("") }
    val viewModel: ShoppingListViewModel =  viewModel()
    val shoppingListState by viewModel.shoppingList.observeAsState()

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
                Row(modifier = Modifier.height(150.dp)) {
                    Column(modifier = Modifier.width(200.dp).fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                        TextField(
                            value = titleText,
                            placeholder = {Text("Title")},
                            onValueChange = {titleText = it},
                            singleLine = true,
                            textStyle = TextStyle(color = Color.Black, fontSize = 13.sp),
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )
                        TextField(
                            value = "",
                            placeholder = {Text("Source")},
                            onValueChange = {titleText = it},
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
                when (shoppingListState) {
                    is ApiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is ApiState.Success<*> -> {
                        val shoppingList = (shoppingListState as ApiState.Success<List<String>>).data
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
                            items(shoppingList) { item ->
                                Row {
                                    TextField(
                                        value = titleText,
                                        placeholder = {Text("Amount")},
                                        onValueChange = {titleText = it},
                                        singleLine = true,
                                        textStyle = TextStyle(color = Color.Black, fontSize = 8.sp),
                                        modifier = Modifier
                                            .width(100.dp)
                                            .padding(end = 5.dp, bottom = 5.dp)
                                            .clip(RoundedCornerShape(20.dp))
                                    )
                                    TextField(
                                        value = titleText,
                                        placeholder = {Text("Ingredient")},
                                        onValueChange = {titleText = it},
                                        singleLine = true,
                                        textStyle = TextStyle(color = Color.Black, fontSize = 12.sp),
                                        modifier = Modifier
                                            .width(170.dp)
                                            .padding(end = 5.dp, bottom = 5.dp)
                                            .clip(RoundedCornerShape(20.dp))
                                    )
                                    Button({}, shape = CircleShape, modifier = Modifier.padding(horizontal = 5.dp)) {
                                        Icon(Icons.Default.Delete, contentDescription = "Remove")
                                    }
                                }
                            }
                            item {
                                Text(text="Steps",
                                    style = TextStyle(color = Color.Black, fontSize = 25.sp),
                                    modifier = Modifier.padding(vertical = 5.dp)
                                )
                            }
                            items(makeLongList(10)) { item ->
                                Row {
                                    TextField(
                                        value = titleText,
                                        placeholder = {Text("Step")},
                                        onValueChange = {titleText = it},
                                        textStyle = TextStyle(color = Color.Black, fontSize = 12.sp),
                                        modifier = Modifier
                                            .padding(end = 5.dp, bottom = 5.dp)
                                            .width(270.dp)
                                            .clip(RoundedCornerShape(20.dp))
                                    )
                                    Button({}, shape = CircleShape) {
                                        Icon(Icons.Default.Delete, contentDescription = "Remove")
                                    }
                                }
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
            }
        }
    }
}
