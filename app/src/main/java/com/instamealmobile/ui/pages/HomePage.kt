package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.ui.Feed
import com.instamealmobile.ui.Menu
import com.instamealmobile.ui.SearchBar
import com.instamealmobile.viewModels.FeedViewModel
import com.instamealmobile.viewModels.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier) {
    val viewModel: FeedViewModel =  viewModel()
    val menuViewModel: MenuViewModel = viewModel()
    val pullToRefreshState = rememberPullToRefreshState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Menu()
            PullToRefreshBox(
                state = pullToRefreshState,
                isRefreshing = viewModel.isRefreshing,
                onRefresh = {
                    viewModel.refreshFeed()
                    menuViewModel.refreshMenu()
                }
            ) {
                Feed()

            }
        }
        SearchBar()
    }
}
