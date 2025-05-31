package com.instamealmobile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.ui.placeholders.FeedPlaceholder


@Composable
fun Feed(feedState:  ApiState<List<Recipe>>?,openConfirmation : (Recipe) -> Unit, openAddRecipe : () -> Unit) {
    when (feedState) {
        is ApiState.Loading -> {
            FeedPlaceholder(openAddRecipe)
        }
        is ApiState.Success<*> -> {
            val feed = (feedState as ApiState.Success<List<Recipe>>).data
            if (feed.isEmpty()) {
                return
            }
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                FeedItem(feed[0], openConfirmation, modifier = Modifier.padding(10.dp))
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
                    items(feed.subList(1,feed.size - 1)) {item ->
                        FeedItem(item, openConfirmation, modifier = Modifier.fillMaxWidth())
                    }
                }
            }

        }
        is ApiState.Error -> {
            val error = feedState.message
            Text(error)
        }

        null -> TODO()

    }
}