package com.instamealmobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.instamealmobile.R
import com.instamealmobile.ui.pages.doSomething

@Composable
fun Header(openHousehold : () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = "InstaMeal",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 10.dp).weight(1f)
        )
        Button(onClick = openHousehold, modifier = Modifier.padding(horizontal = 5.dp)) {
            Icon(painter = painterResource(R.drawable.household__2_), "Household")
        }
    }
}
