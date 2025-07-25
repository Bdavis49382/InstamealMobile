package com.instamealmobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.instamealmobile.OpenSheet
import com.instamealmobile.data.Recipe
import com.instamealmobile.data.RecipeIdentifier
import com.instamealmobile.viewModels.NavViewModel

@Composable
fun FeedItem(recipe: Recipe, modifier: Modifier = Modifier, intrinsic: Boolean = false) {
    val nav: NavViewModel = viewModel()
    val painter = rememberAsyncImagePainter(model = if (!recipe.img_link.isNullOrEmpty()) recipe.img_link else  "https://placehold.co/600x400.png?text=${recipe.title}")
    val aspectRatio = when (painter.state) {
        is AsyncImagePainter.State.Success -> {
            val size = painter.state.painter?.intrinsicSize
            if (size != null && intrinsic && size.width > 0 && size.height > 0) size.width / size.height else 1f

        }
        else -> 1f
    }
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
                Image(
                    painter = painter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(aspectRatio)
                        .clip(RoundedCornerShape(10.dp)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}