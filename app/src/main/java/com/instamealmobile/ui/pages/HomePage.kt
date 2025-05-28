package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.data.Recipe
import com.instamealmobile.ui.Feed
import com.instamealmobile.ui.Header
import com.instamealmobile.ui.Menu
import com.instamealmobile.ui.SearchBar
import com.instamealmobile.viewModels.FeedViewModel

@Composable
fun HomePage(openConfirmation: (meal : Recipe) -> Unit, openRecipe: (meal : Recipe) -> Unit, openAddRecipe: () -> Unit, openHousehold: () -> Unit, modifier : Modifier = Modifier) {
    val viewModel: FeedViewModel =  viewModel()
    val feedState by viewModel.feed.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFeed()
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Header(openHousehold)
            Menu(openRecipe)
            Feed(feedState, openConfirmation, openAddRecipe)
        }
        SearchBar(viewModel)
    }
}
