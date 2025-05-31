package com.instamealmobile.ui.placeholders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun CheckItemPlaceholder() {
    Row (
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier
            .size(40.dp)
            .shimmer()
            .background(color = Color.LightGray, shape = CircleShape)
        )
        Column {
            Box(modifier = Modifier
                .padding(start = 10.dp)
                .width(250.dp)
                .height(10.dp)
                .shimmer()
                .background(Color.LightGray)
            )
            Box(modifier = Modifier
                .width(150.dp)
                .height(10.dp)
                .padding(start = 10.dp, top=5.dp)
                .shimmer()
                .background(Color.LightGray)
            )
        }
        Box(modifier = Modifier
            .width(80.dp)
            .height(30.dp)
            .padding(start=40.dp, end = 10.dp)
            .shimmer()
            .background(Color.LightGray)
        )
    }
}