package com.instamealmobile.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.LoadState.NotLoading
import androidx.paging.compose.collectAsLazyPagingItems
import com.instamealmobile.ui.placeholders.FeedPlaceholder
import com.instamealmobile.viewModels.AuthViewModel
import com.instamealmobile.viewModels.FeedViewModel
import com.instamealmobile.viewModels.MenuViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Feed() {
    val viewModel : FeedViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val menuViewModel: MenuViewModel = viewModel()
    if (authViewModel.checkLogin()) {
        val items = viewModel.pagingFlow.collectAsLazyPagingItems()
        val pullToRefreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            state = pullToRefreshState,
            isRefreshing = items.loadState.refresh is LoadState.Loading,
            onRefresh = {
                menuViewModel.refreshMenu()
                viewModel.query.value = ""
                items.refresh()
            }
        ) {
            Crossfade(targetState = items.loadState.refresh, label = "ContentSwitch") { screenState ->
                when (screenState) {
                    LoadState.Loading -> {
                        FeedPlaceholder()
                    }
                    is NotLoading ->  {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(horizontal = 15.dp, vertical = 0.dp),
                                horizontalArrangement = Arrangement.spacedBy(15.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .height(700.dp)
                            ) {
                                if (items.itemCount >= 1) {
                                    item(span = { GridItemSpan(2)}) {

                                        val item = items[0]
                                        item?.let {FeedItem(item)}
                                    }
                                } else {
                                    item(span = { GridItemSpan(2)}) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                "No Recipes To Show",
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            Button({
                                                viewModel.query.value = ""
                                                items.refresh() }) {
                                                Text("Refresh Feed")
                                            }

                                        }
                                    }
                                    item {

                                    }

                                }
                                if (items.itemCount >= 2) {
                                    item {
                                        AddRecipeButton()
                                    }
                                    item(span = { GridItemSpan(1)}) {
                                        val item = items[1]
                                        item?.let {FeedItem(item)}
                                    }
                                }
                                items(items.itemCount) { index ->
                                    if (index > 1) {
                                        val item = items[index]
                                        item?.let { FeedItem(item, modifier = Modifier.fillMaxWidth()) }
                                    }
                                }
                                when (items.loadState.append) {
                                    is LoadState.Loading -> {
                                        item { Text("Loading...") }
                                    }

                                    is LoadState.Error -> {
                                        item {
                                            Button(items::refresh) {
                                                Text("Try Again")
                                            }
                                        }
                                    }

                                    else -> {}
                                }
                                item(span = { GridItemSpan(2)}) {
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                        Text("No More Search Results.", modifier = Modifier.padding(top = 10.dp, bottom = 200.dp))
                                    }
                                }
                            }
                        }

                    }

                    is LoadState.Error -> {
                        val error = screenState.error.message
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(error?: "An error occurred.")
                            Button(items::refresh) {
                                Text("Try Again")
                            }

                        }
                    }
                }
            }
        }
    } else {
        FeedPlaceholder()
    }
}