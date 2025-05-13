package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.instamealmobile.ui.Feed
import com.instamealmobile.ui.Header
import com.instamealmobile.ui.Menu
import com.instamealmobile.ui.SearchBar

fun doSomething() : Unit {
    return
}

@Composable
fun HomePage(openConfirmation: (meal : String) -> Unit, openRecipe: (meal : String) -> Unit, openAddRecipe: () -> Unit, openHousehold: () -> Unit, modifier : Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Header(openHousehold)
            Menu(openRecipe)
            Feed(openConfirmation, openAddRecipe)
        }
        SearchBar()
    }
}
