package com.instamealmobile.ui.placeholders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerBox(width: Dp = 0.dp, height: Dp = 0.dp, fillmaxWidth: Boolean = false, fillmaxHeight: Boolean = false, modifier: Modifier = Modifier) {
    if (!fillmaxHeight && !fillmaxWidth) {
        Box(modifier= modifier
            .width(width)
            .height(height)
            .shimmer()
            .background(Color.LightGray)
        )
    }
    else if (!fillmaxHeight) {
        Box(modifier= modifier
            .fillMaxWidth()
            .height(height)
            .shimmer()
            .background(Color.LightGray)
        )
    } else if (!fillmaxWidth){
        Box(modifier= modifier
            .width(width)
            .fillMaxHeight()
            .shimmer()
            .background(Color.LightGray)
        )
    } else {
        Box(modifier= modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .shimmer()
            .background(Color.LightGray)
        )
    }
}