package com.instamealmobile.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.instamealmobile.R

@Composable
fun MealList(mealList: List<String>, modifier: Modifier = Modifier) {
    val chalkFont = FontFamily(
        Font(R.font.caveat_brush, FontWeight.Normal)
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 120.dp)
        ,
    ) {
        items(mealList) {meal ->
            CheckItem(name = meal, color = colorResource(R.color.chalk), fontFamily = chalkFont)
        }

    }
}
