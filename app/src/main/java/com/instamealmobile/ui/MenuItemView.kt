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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.instamealmobile.OpenSheet
import com.instamealmobile.data.MenuListItem
import com.instamealmobile.data.RecipeIdentifier.MenuIndex
import com.instamealmobile.viewModels.NavViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MenuItemView(menuListItem: MenuListItem) {
    val nav: NavViewModel = viewModel()
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
        else {
            Text("")
        }
        AsyncImage(
            model = menuListItem.img_link,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable { nav.navigateTo(OpenSheet.ViewRecipe, MenuIndex(menuListItem.index?:0))},
            contentDescription = null
        )
        Text(menuListItem.title,overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(200.dp)
        )
    }
}