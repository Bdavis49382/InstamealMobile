package com.instamealmobile.ui

import Tag
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.instamealmobile.data.Recipe

@Composable
fun RecipeView(lazyListState: LazyListState, recipe: Recipe, innerContent: @Composable () -> Unit = {}) {
    val painter = rememberAsyncImagePainter(model = if (!recipe.img_link.isNullOrEmpty()) recipe.img_link else  "https://placehold.co/600x400.png?text=${recipe.title}")
    val aspectRatio = when (painter.state) {
        is AsyncImagePainter.State.Success -> {
            val size = painter.state.painter?.intrinsicSize
            if (size != null && size.width > 0 && size.height > 0) size.width / size.height else 1f

        }
        else -> 1f
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(20.dp)
    ) {
        Text(
            text = recipe.title,
            style = TextStyle( fontSize = 30.sp),
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    items(recipe.tags) { text ->
                        Tag(text, modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                        )
                    }
                }
            }
            item {
                if (recipe.src_link.isNullOrEmpty()) {
                    Text(text=recipe.src_name?:"")
                } else {
                    Text(
                        buildAnnotatedString {
                            withLink(
                                link = LinkAnnotation.Url(
                                    recipe.src_link,
                                    TextLinkStyles(style = SpanStyle(color = Color.Blue, fontSize = 15.sp))
                                )
                            ) {
                                append(recipe.src_name)
                            }
                        },
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
            item {
                Image(
                    painter = painter,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(aspectRatio)
                        .clip(RoundedCornerShape(10.dp)),
                    contentDescription = null
                )

            }
            item {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
                    if (recipe.servings != null) {
                        Text(text="${recipe.servings} Servings")
                    }
                    if (recipe.time_estimate.isNotEmpty()) {
                        Text("Total Time: ${recipe.time_estimate[0]}")
                    }
                }
            }
            item {
                innerContent()
            }
            item {
                Text(
                    text = "Ingredients",
                    style = TextStyle( fontSize = 28.sp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                )

            }
            items(recipe.ingredients) { item ->
                Text(
                    text = item,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                )
            }
            item {
                Text(
                    text = "Steps",
                    style = TextStyle( fontSize = 28.sp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                )
            }
            itemsIndexed(recipe.instructions) { index,item ->
                Text(
                    text = "${index + 1}. $item",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                )
            }
            item {
                Spacer(modifier = Modifier
                    .padding(40.dp)
                )
            }
        }
    }
}
