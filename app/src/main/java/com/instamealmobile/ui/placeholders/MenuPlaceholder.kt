package com.instamealmobile.ui.placeholders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.instamealmobile.R

@Composable
fun MenuPlaceholder() {
    Box(modifier = Modifier
        .height(200.dp)
        .fillMaxWidth()
        .padding(vertical=10.dp)
    ){
        Row {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxHeight()
                .width(40.dp)
            ) {
                Icon(painter= painterResource(R.drawable.menu),"Menu", modifier=Modifier.align(Alignment.Center))
            }

            LazyRow(
                //                        reverseLayout = true,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsIndexed(listOf(1,1,1)) { index,item ->
                    MenuItemViewPlaceholder()
                }
            }
        }
    }
}