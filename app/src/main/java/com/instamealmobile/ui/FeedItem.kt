package com.instamealmobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.OpenSheet
import com.instamealmobile.data.Recipe
import com.instamealmobile.data.RecipeIdentifier
import com.instamealmobile.viewModels.NavViewModel

@Composable
fun FeedItem(recipe: Recipe, modifier: Modifier = Modifier, intrinsic: Boolean = false) {
    val nav: NavViewModel = viewModel()
    Box(
        modifier = modifier
            .clickable { nav.navigateTo(OpenSheet.PreviewRecipe,RecipeIdentifier.factory(recipe)) }
    ) {
        Column {
            Text(text=recipe.title,
                fontSize = 13.sp,
                color= MaterialTheme.colorScheme.onBackground,
                lineHeight = 13.sp,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(start = 5.dp),
            )
            Box(contentAlignment = Alignment.TopEnd) {
                SmartAsyncImage(recipe.img_link, backupText = recipe.title, intrinsic = intrinsic)
            }
        }
    }
}