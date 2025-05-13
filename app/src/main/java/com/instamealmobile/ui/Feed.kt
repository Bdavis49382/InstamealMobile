package com.instamealmobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instamealmobile.makeLongList


@Composable
fun Feed(openConfirmation : (String) -> Unit, openAddRecipe : () -> Unit) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        FeedItem("Cheeseburger", openConfirmation, modifier = Modifier.padding(10.dp))
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Adaptive(minSize = 180.dp),
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
            items(makeLongList(10)) {item ->
                FeedItem(item, openConfirmation)
            }
        }
    }
}