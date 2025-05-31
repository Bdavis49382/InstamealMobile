package com.instamealmobile.ui.placeholders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun RecipeViewPlaceholder() {
    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {
        Row(modifier = Modifier) {
            Column(
                modifier = Modifier.width(200.dp),
                verticalArrangement = Arrangement.Center
            ) {
                ShimmerBox(fillmaxWidth = true, height = 15.dp, modifier = Modifier
                    .padding(10.dp)
                )
                ShimmerBox(fillmaxWidth = true, height = 10.dp, modifier = Modifier
                    .padding(horizontal = 10.dp)
                )
            }
            ShimmerBox(width = 400.dp, height = 200.dp, modifier = Modifier
                .clip(RoundedCornerShape(10.dp))

            )
        }
        ShimmerBox(width=50.dp, height=10.dp, modifier = Modifier
            .padding(5.dp)
        )
        ShimmerBox(width=50.dp, height=10.dp, modifier = Modifier
            .padding(5.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                ShimmerBox(width=100.dp,height=20.dp,
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                )

            }
            items(listOf(1,1,1,1,1,1)) { item ->
                ShimmerBox(fillmaxWidth = true, height=15.dp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
            item {
                ShimmerBox(width=100.dp,height=20.dp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
            items(listOf(1,1,1,1,1,1,1)) { item ->
                ShimmerBox(fillmaxWidth = true,height=15.dp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}