package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.instamealmobile.ui.Feed
import com.instamealmobile.ui.Menu
import com.instamealmobile.ui.SearchBar

@Composable
fun HomePage(modifier: Modifier = Modifier) {

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Menu()
            Feed()
        }
        SearchBar()
    }
}
