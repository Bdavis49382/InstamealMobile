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
import com.instamealmobile.data.Recipe
import com.instamealmobile.ui.Feed
import com.instamealmobile.ui.Header
import com.instamealmobile.ui.Menu
import com.instamealmobile.ui.SearchBar
import com.instamealmobile.viewModels.FeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(openConfirmation: (meal : Recipe) -> Unit, openRecipe: (meal : Recipe) -> Unit, openAddRecipe: () -> Unit, openHousehold: () -> Unit, openShoppingList: () -> Unit, modifier : Modifier = Modifier) {
    val viewModel: FeedViewModel =  viewModel()
    val pullToRefreshState = rememberPullToRefreshState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Header(openHousehold)
            Menu(openRecipe)
            PullToRefreshBox(
                state = pullToRefreshState,
                isRefreshing = viewModel.isRefreshing,
                onRefresh = viewModel::refreshFeed
            ) {
                Feed(openConfirmation, openAddRecipe)

            }
        }
        SearchBar(viewModel, openShoppingList)
    }
}
