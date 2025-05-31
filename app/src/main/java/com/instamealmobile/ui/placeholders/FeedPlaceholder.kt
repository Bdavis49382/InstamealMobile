package com.instamealmobile.ui.placeholders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.instamealmobile.ui.AddRecipeButton
import com.valentinilk.shimmer.shimmer

@Composable
fun FeedPlaceholder(openAddRecipe: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .shimmer()
            .height(400.dp)
            .background(Color.LightGray)
        )
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalItemSpacing = 15.dp,
            modifier = Modifier
                .padding(top = 10.dp)
                .height(700.dp)
        ) {
            item {
                AddRecipeButton(openAddRecipe)
            }
            items(mutableListOf(1, 1, 1)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .height(200.dp)
                        .shimmer()
                        .background(Color.LightGray)
                )
            }
        }
    }

}
