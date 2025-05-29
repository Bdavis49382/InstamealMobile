package com.instamealmobile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.instamealmobile.data.Recipe

@Composable
fun RecipeView(recipe: Recipe) {
    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {
        Row(modifier = Modifier) {
            Column(
                modifier = Modifier.width(200.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = recipe.title,
                    style = TextStyle( fontSize = 30.sp),
                    modifier = Modifier.fillMaxWidth()
                )
                if (recipe.src_link.isNullOrEmpty()) {
                    Text(text=recipe.src_name?:"")
                } else {
                    Text(
                        buildAnnotatedString {
                            withLink(
                                link = LinkAnnotation.Url(
                                    recipe.src_link,
                                    TextLinkStyles(style = SpanStyle(color = Color.Blue))
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
            Box(modifier = Modifier.width(400.dp)) {
                AsyncImage(
                    model = if (!recipe.img_link.isNullOrEmpty()) recipe.img_link else  "https://recipe-graphics.grocerywebsite.com/0_GraphicsRecipes/4589_4k.jpg",
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp)),
                    contentDescription = null
                )

            }
        }
        if (recipe.servings != null) {
            Text(text="${recipe.servings.toInt()} Servings")
        }
        if (recipe.time_estimate.isNotEmpty()) {
            Text("Total Time: ${recipe.time_estimate[0]}")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                Text(
                    text = "Ingredients",
                    style = TextStyle( fontSize = 25.sp),
                    modifier = Modifier.fillMaxWidth()
                )

            }
            items(recipe.ingredients) { item ->
                Text(
                    text = item,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                )
            }
            item {
                Text(
                    text = "Steps",
                    style = TextStyle(fontSize = 25.sp),
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
            items(recipe.instructions) { item ->
                Text(
                    text = item,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                )
            }
        }
    }
}
