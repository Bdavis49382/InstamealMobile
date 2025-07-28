package com.instamealmobile.ui.placeholders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.instamealmobile.ui.AddRecipeButton

@Composable
fun FeedPlaceholder() {
    Column {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(top = 10.dp)
                .height(700.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                ShimmerBox(fillmaxWidth = true, height = 300.dp)
            }
            item {
                AddRecipeButton()
            }
            items(3) {
                ShimmerBox(fillmaxWidth = true, height = 200.dp)
            }
        }
    }

}
