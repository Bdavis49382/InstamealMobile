package com.instamealmobile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.viewModels.FeedViewModel

@Composable
fun SearchBar(viewModel:  FeedViewModel) {
    var searchBoxText by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        TextField(
            value = searchBoxText,
            onValueChange = {searchBoxText = it},
            leadingIcon = { Icon(Icons.Default.Search, "Search Icon") },
            placeholder = { Text("Search Recipes") },
            keyboardOptions = KeyboardOptions(imeAction= ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                viewModel.searchFeed(searchBoxText)
                searchBoxText = ""
            }),
            singleLine = true,
            textStyle = TextStyle(color = Color.Black, fontSize = 20.sp),
            modifier = Modifier
                .padding(start = 5.dp, top = 15.dp, end = 120.dp, bottom = 30.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
        )
    }
}