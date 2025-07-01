package com.instamealmobile.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeleteButton(onClick: () -> Unit) {
    TextButton(
        onClick = onClick
    ) {
        Icon(
            Icons.Default.Close,
            contentDescription = "Remove Item",
            modifier = Modifier
        )
    }

}
