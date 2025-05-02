package com.instamealmobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instamealmobile.R
import com.instamealmobile.makeLongList

fun doSomething() : Unit {
    return
}

@Composable
fun MealsArea(openConfirmation: (meal : String) -> Unit, openRecipe: (meal : String) -> Unit, modifier : Modifier = Modifier) {
    var searchBoxText by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer)
            ){
                Text(
                    text = "InstaMeal",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 10.dp))
                Button(onClick=::doSomething) {
                    Icon(painter= painterResource(R.drawable.household__2_),"Household")
                }
            }
            Box(modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .padding(vertical=10.dp)
            ){
                LazyRow(
//                        reverseLayout = true,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .fillMaxHeight()
                            .width(40.dp)
                        ) {
                            Icon(painter= painterResource(R.drawable.menu),"Menu", modifier=Modifier.align(Alignment.Center))
                        }
                    }
                    items(makeLongList()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("today")
                            AsyncImage(
                                model = "https://recipe-graphics.grocerywebsite.com/0_GraphicsRecipes/4589_4k.jpg",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                    .clickable { openRecipe(it) },
                                contentDescription = null
                            )
                            Text(it)

                        }
                    }
                }
            }
            LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Adaptive(minSize = 180.dp),
                contentPadding = PaddingValues(horizontal = 15.dp, vertical = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalItemSpacing = 15.dp,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .height(700.dp)
            ) {
                items(makeLongList(10)) {item ->
                    Box(
                        modifier = Modifier
                            .clickable { openConfirmation(item) }
                    ) {
                        Column {
                            Text(text=item,
                                fontSize = 13.sp,
                                color= MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier
                                    .padding(start = 5.dp),
                            )
                            AsyncImage(
                                model = "https://recipe-graphics.grocerywebsite.com/0_GraphicsRecipes/4589_4k.jpg",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp)),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            TextField(
                value = searchBoxText,
                onValueChange = {searchBoxText = it},
                leadingIcon = { Icon(Icons.Default.Search, "Search Icon") },
                placeholder = { Text("Search Recipes") },
                singleLine = true,
                textStyle = TextStyle(color = Color.Black, fontSize = 20.sp),
                modifier = Modifier
                    .padding(start = 5.dp, top = 15.dp, end = 120.dp, bottom = 30.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
            )
        }

    }
}
