package com.instamealmobile.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.data.ScreenState
import com.instamealmobile.ui.placeholders.FeedPlaceholder
import com.instamealmobile.viewModels.FeedViewModel


@Composable
fun Feed(openConfirmation : (Recipe) -> Unit, openAddRecipe : () -> Unit) {
    val viewModel : FeedViewModel = viewModel()
    val feedState by viewModel.feed.collectAsState(ApiState.Loading)

    LaunchedEffect(true) {
        viewModel.fetchFeed()
    }

    val screenState = when (feedState) {
        is ApiState.Loading -> ScreenState.Loading
        is ApiState.Success -> ScreenState.Success
        is ApiState.Resting -> ScreenState.Resting
        is ApiState.Error -> ScreenState.Error
    }
    Crossfade(targetState = screenState, label = "ContentSwitch") { screenState ->
        when (screenState) {
            ScreenState.Loading ->  {
                FeedPlaceholder(openAddRecipe)
            }
            ScreenState.Success -> if (feedState is ApiState.Success) {
                val feed = (feedState as ApiState.Success<List<Recipe>>).data
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (feed.isEmpty()) {
                        Text(
                            "No Recipes To Show",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button({ viewModel.refreshFeed() }) {
                            Text("Refresh Feed")
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 0.dp),
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .height(700.dp)
                        ) {
                            item(span = { GridItemSpan(2)}) {
                                FeedItem(feed[0], openConfirmation)
                            }
                            item {
                                AddRecipeButton(openAddRecipe)
                            }
                            if (feed.size > 1) {
                                items(feed.subList(1, feed.size - 1)) { item ->
                                    FeedItem(item, openConfirmation, modifier = Modifier.fillMaxWidth())
                                }
                            }
                        }
                    }
                }

            }
            ScreenState.Error -> if (feedState is ApiState.Error) {
                val error = (feedState as ApiState.Error).message
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(error)
                    Button(viewModel::refreshFeed) {
                        Text("Try Again")
                    }

                }
            }

            else -> {}

        }
    }
}