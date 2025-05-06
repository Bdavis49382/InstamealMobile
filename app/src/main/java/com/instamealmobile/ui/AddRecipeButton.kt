package com.instamealmobile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instamealmobile.R

@Composable
fun AddRecipeButton(openAddRecipePage: () -> Unit) {
    ElevatedCard {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clickable { openAddRecipePage() }
        ) {
            AsyncImage(
                model = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRfajWqibp3QaI9ltOy2CmoqbljCae0PdkCww&s",
                modifier = Modifier
                    .graphicsLayer { this.alpha = 0.5f}
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                contentDescription = null
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.BottomCenter) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer )
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(45.dp)
                            .height(45.dp)
                            .clip(CircleShape)
                            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimaryContainer), CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Text(text="+",
                            fontSize = 20.sp,
                            color= MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                    }

                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer )
                ) {
                    Text(text="Add Your Own Recipe To Your Feed",
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color= MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 5.dp),
                    )
                }
            }
        }
    }
}