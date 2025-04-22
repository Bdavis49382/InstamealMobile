package com.instamealmobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instamealmobile.R
import com.instamealmobile.makeLongList

@Composable
fun MealsArea(openConfirmation: (meal : String) -> Unit, openRecipe: (meal : String) -> Unit, modifier : Modifier = Modifier) {
    var searchBoxText by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Select a Meal to Get Started!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(10.dp))
            LazyHorizontalGrid(rows = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 15.dp, vertical = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .height(400.dp)
            ) {
                items(makeLongList(10)) {item ->
                    ElevatedCard(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .size(
                                width = 225.dp, height = 150.dp
                            )
                            .clickable { openConfirmation(item) }
                        ,
                    ) {
                        Column {
                            Text(text=item,
                                color= MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(10.dp)
                            )
                            AsyncImage(
                                model = "https://recipe-graphics.grocerywebsite.com/0_GraphicsRecipes/4589_4k.jpg",
                                modifier = Modifier
                                    .size(200.dp)
                                    .padding(start = 5.dp),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
            Box(modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
            ){
                Column {
                    Text(
                        text = "My Meals",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 15.dp))
                    LazyRow(
                        reverseLayout = true,
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(makeLongList()) {
                            AsyncImage(
                                model = "https://recipe-graphics.grocerywebsite.com/0_GraphicsRecipes/4589_4k.jpg",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clickable { openRecipe(it) },
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
                label = { Text("Search") },
                placeholder = { Text("Search Powered by GPT...") },
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
