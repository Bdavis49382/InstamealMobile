package com.instamealmobile.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun MenuItem(recipe: String, openRecipe: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("today")
        AsyncImage(
            model = "https://recipe-graphics.grocerywebsite.com/0_GraphicsRecipes/4589_4k.jpg",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable { openRecipe(recipe) },
            contentDescription = null
        )
        Text(recipe)
    }
}