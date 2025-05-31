package com.instamealmobile.ui.placeholders

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShoppingListPlaceholder() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {
        itemsIndexed(listOf(1,1,1,1,1,1)) { index,item ->
            CheckItemPlaceholder()
        }
    }
}