package com.instamealmobile.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.instamealmobile.viewModels.RecipeViewModel

@Composable
fun RecipeView(recipe: Recipe) {
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
                    model = recipe.img_link
                        ?: "https://recipe-graphics.grocerywebsite.com/0_GraphicsRecipes/4589_4k.jpg",
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp)),
                    contentDescription = null
                )

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
                    modifier = Modifier.fillMaxWidth()
                )

            }
            items(recipe.ingredients) { item ->
                Text(
                    text = item,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                )
            }
            item {
                Text(
                    text = "Steps",
                    style = TextStyle(color = Color.Black, fontSize = 25.sp),
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
            items(recipe.instructions) { item ->
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
