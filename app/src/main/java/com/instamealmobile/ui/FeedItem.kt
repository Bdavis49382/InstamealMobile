package com.instamealmobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun FeedItem(recipe: String, openConfirmation: (String) -> Unit, modifier: Modifier = Modifier) {

    Box(
        modifier = modifier
            .clickable { openConfirmation(recipe) }
    ) {
        Column {
            Text(text=recipe,
                fontSize = 15.sp,
                color= MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(start = 5.dp),
            )
            AsyncImage(
                model = "https://recipe-graphics.grocerywebsite.com/0_GraphicsRecipes/4589_4k.jpg",
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp)),
                contentDescription = null
            )
        }
    }
}