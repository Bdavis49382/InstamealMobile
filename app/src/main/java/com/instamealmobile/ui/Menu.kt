package com.instamealmobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.instamealmobile.R
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.MenuItem
import com.instamealmobile.data.Recipe
import com.instamealmobile.data.ShoppingItem
import com.instamealmobile.data.SmallShoppingItem
import com.instamealmobile.makeLongList
import com.instamealmobile.viewModels.MenuViewModel

@Composable
fun Menu(openRecipe: (Recipe) -> Unit) {
    val viewModel: MenuViewModel =  viewModel()
    val menuState by viewModel.menu.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.getMenu()
    }
    when (menuState) {
        is ApiState.Loading -> {
            CircularProgressIndicator()
        }
        is ApiState.Success<*> -> {
            val menu = (menuState as ApiState.Success<List<MenuItem>>).data
            Box(modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(vertical=10.dp)
            ){
                Row {
                    Box(modifier = Modifier
                        .clip(RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxHeight()
                        .width(40.dp)
                    ) {
                        Icon(painter= painterResource(R.drawable.menu),"Menu", modifier=Modifier.align(Alignment.Center))
                    }

                    LazyRow(
                        //                        reverseLayout = true,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(menu) {
                            MenuItemView(it, openRecipe)
                        }
                    }
                }
            }
        }
        is ApiState.Error -> {
            val error = (menuState as ApiState.Error).message
            Text(error)
        }

        null -> TODO()
    }
}