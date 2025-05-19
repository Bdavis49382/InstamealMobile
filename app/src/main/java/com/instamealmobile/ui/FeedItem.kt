package com.instamealmobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instamealmobile.data.Recipe

@Composable
fun FeedItem(recipe: Recipe, openConfirmation: (String) -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clickable { openConfirmation(recipe.title) }
    ) {
        Column {
            Text(text=recipe.title,
                fontSize = 11.sp,
                color= MaterialTheme.colorScheme.onPrimaryContainer,
                lineHeight = 11.sp,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(start = 5.dp),
            )
            Box(contentAlignment = Alignment.TopEnd) {
                AsyncImage(
                    model = recipe.img_link,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(10.dp)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
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