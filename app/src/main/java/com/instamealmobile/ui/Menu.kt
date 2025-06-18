package com.instamealmobile.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.R
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.MenuListItem
import com.instamealmobile.data.Recipe
import com.instamealmobile.ui.placeholders.MenuPlaceholder
import com.instamealmobile.viewModels.MenuViewModel

@Composable
fun Menu(openRecipe: (Recipe) -> Unit) {
    val viewModel: MenuViewModel =  viewModel()
    val menuState by viewModel.menu.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getMenu()
    }
    Crossfade(targetState = menuState) {menuState ->
        Box(modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .padding(vertical=10.dp)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxHeight()
                        .width(40.dp)
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        painter = painterResource(R.drawable.menu),
                        contentDescription = "Menu",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                when (menuState) {
                    is ApiState.Loading -> {
                        MenuPlaceholder()
                    }

                    is ApiState.Success<*> -> {
                        val menu = (menuState as ApiState.Success<List<MenuListItem>>).data
                        LazyRow(
                            //                        reverseLayout = true,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (menu.isEmpty()) {
                                item {
                                    Text(
                                        "Add or Choose Recipes to Start Your Menu!",
                                        modifier = Modifier.padding(start = 10.dp)
                                    )
                                }
                            } else {
                                itemsIndexed(menu) { index, item ->
                                    MenuItemView(item, openRecipe, index)
                                }
                            }
                        }
                    }

                    is ApiState.Error -> if (menuState is ApiState.Error) {
                        val error = (menuState as ApiState.Error).message
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(error)
                            Button(
                                viewModel::getMenu
                            ) {
                                Text("Try Again")
                            }

                        }
                    }

                    else -> {}
                }
            }
        }
    }
}