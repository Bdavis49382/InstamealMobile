package com.instamealmobile.ui.placeholders

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer


@Composable
fun MenuItemViewPlaceholder() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier
            .shimmer()
            .background(Color.LightGray)
            .width(30.dp)
            .height(10.dp)
        )
        Box(
            modifier = Modifier
                .size(120.dp)
                .padding(10.dp)
                .shimmer()
                .clip(CircleShape)
                .background(Color.LightGray)
                .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Box(modifier = Modifier
            .shimmer()
            .background(Color.LightGray)
            .width(140.dp)
            .height(10.dp)
        )
    }

}
