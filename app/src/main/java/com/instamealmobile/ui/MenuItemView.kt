package com.instamealmobile.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.instamealmobile.data.MenuListItem
import com.instamealmobile.data.Recipe
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MenuItemView(menuListItem: MenuListItem, openRecipe: (Recipe) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (menuListItem.date != null) {
            val localDate = menuListItem.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            if (localDate.equals(LocalDate.now())) {
                Text("Today")
            } else {
                Text(localDate.dayOfWeek.getDisplayName(
                    TextStyle.FULL,Locale.getDefault()))
            }
        }
        AsyncImage(
            model = menuListItem.img_link,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable { openRecipe(Recipe(title="",id=menuListItem.recipe_id,index=menuListItem.index?: 0)) },
            contentDescription = null
        )
        Text(menuListItem.title,overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(200.dp)
        )
    }
}