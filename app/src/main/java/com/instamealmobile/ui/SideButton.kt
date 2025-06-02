package com.instamealmobile.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SideButton(onClick: () -> Unit,modifier: Modifier = Modifier, content: @Composable() () -> Unit) {
    Button(
        onClick,
        contentPadding = PaddingValues(start = 8.dp, end= 4.dp, bottom = 8.dp, top=8.dp),
        shape = RoundedCornerShape(bottomStart = 10.dp, topStart = 10.dp),
        modifier = modifier
    ) {
        content()
    }

}