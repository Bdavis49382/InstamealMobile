package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.instamealmobile.R
import com.instamealmobile.data.Recipe
import com.instamealmobile.ui.DatePickerModal
import com.instamealmobile.ui.SideButton
import com.instamealmobile.viewModels.MenuViewModel

@Composable
fun AddRecipeToMenu(recipe : Recipe, confirm: () -> Unit) {
    val viewModel: MenuViewModel =  viewModel()

    LaunchedEffect(Unit) {
        viewModel.ingredients.clear()
        viewModel.ingredients.addAll(recipe.ingredients)
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
                    AsyncImage(
                        model = recipe.img_link ?: "https://recipe-graphics.grocerywebsite.com/0_GraphicsRecipes/4589_4k.jpg",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(10.dp)),
                        contentDescription = null
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
                        TextButton(
                            onClick = {viewModel.ingredients.removeAt(index)}
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remove Item",
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 200.dp)
        ) {
            SideButton({viewModel.addRecipe(recipe)
                confirm()}
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Edit")
            }
        }
    }
}
