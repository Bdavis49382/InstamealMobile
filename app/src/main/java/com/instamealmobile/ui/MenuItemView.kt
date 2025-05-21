package com.instamealmobile.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.Recipe

@Composable
fun MenuItemView(menuItem: MenuItem, openRecipe: (Recipe) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("today")
        AsyncImage(
            model = menuItem.img_link ?: "https://recipe-graphics.grocerywebsite.com/0_GraphicsRecipes/4589_4k.jpg",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable { openRecipe(Recipe(title="",id=menuItem.recipe_id)) },
            contentDescription = null
        )
        Text(menuItem.title,
//            modifier = Modifier.width(150.dp)
        )
    }
}