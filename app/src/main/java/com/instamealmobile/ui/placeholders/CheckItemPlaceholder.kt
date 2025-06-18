package com.instamealmobile.ui.placeholders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun CheckItemPlaceholder() {
    Row (
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ShimmerBox(width=40.dp, height=40.dp, modifier= Modifier.clip(shape = CircleShape))
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            ShimmerBox(width=250.dp, height=10.dp)
            ShimmerBox(width=150.dp, height=10.dp)
        }
        ShimmerBox(width=40.dp, height=40.dp, modifier = Modifier.padding(start = 30.dp))
    }
}