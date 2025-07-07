package com.instamealmobile.ui.placeholders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(20.dp)
    ) {
        ShimmerBox(width = 300.dp, height = 25.dp)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
            ShimmerBox(width = 120.dp, height = 15.dp)
            ShimmerBox(width = 120.dp, height = 15.dp)
        }
        ShimmerBox(width = 100.dp, height = 10.dp)
        ShimmerBox(fillmaxWidth = true, height = 300.dp, modifier = Modifier
            .clip(RoundedCornerShape(10.dp))

        )
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
            ShimmerBox(width=50.dp, height=15.dp)
            ShimmerBox(width=50.dp, height=15.dp)
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                ShimmerBox(width=100.dp,height=20.dp,
                    modifier = Modifier.padding(top = 5.dp)
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
                        .padding(vertical = 5.dp)
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