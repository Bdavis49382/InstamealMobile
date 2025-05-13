package com.instamealmobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
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
            Box(contentAlignment = Alignment.TopEnd) {
                AsyncImage(
                    model = "https://recipe-graphics.grocerywebsite.com/0_GraphicsRecipes/4589_4k.jpg",
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp)),
                    contentDescription = null
                )
                Box(contentAlignment = Alignment.Center, modifier = Modifier
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(5.dp))
                    .graphicsLayer {
                        this.alpha = 0.9f
                    }
                    .width(55.dp)
                    .height(25.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier
                        .width(50.dp)
                        .height(20.dp)
                    ) {
                        Text("2:45 ",
                            fontSize = 13.sp,
                            letterSpacing = 0.sp,
                            modifier = Modifier
                        )
                        Icon(painter = painterResource(com.instamealmobile.R.drawable.history), contentDescription = "Clock Icon")
                    }
                }
            }
        }
    }
}