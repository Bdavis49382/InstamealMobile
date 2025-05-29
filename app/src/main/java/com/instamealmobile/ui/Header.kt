package com.instamealmobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.instamealmobile.R

@Composable
fun Header(openHousehold : () -> Unit) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = "Easy Meals",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 10.dp).weight(1f)
        )
        Button(onClick = openHousehold, modifier = Modifier.padding(horizontal = 5.dp)) {
            Icon(painter = painterResource(R.drawable.household__2_), "Household")
        }
    }
}
