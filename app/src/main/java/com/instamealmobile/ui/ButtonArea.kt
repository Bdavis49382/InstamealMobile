package com.instamealmobile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.instamealmobile.OpenSheet

@Composable
fun ButtonArea(setShowSheet : (OpenSheet) -> Unit, modifier : Modifier = Modifier) {
    Box (
        modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            Button(onClick = { setShowSheet(OpenSheet.Share)},
                shape = CircleShape,
                modifier = Modifier.size(80.dp),
                border = BorderStroke(2.dp, Color.Gray),
            ) {
                Icon(Icons.Default.Share, contentDescription = "Share")
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { setShowSheet(OpenSheet.ShoppingList)},
                shape = CircleShape,
                modifier = Modifier.size(80.dp),
                border = BorderStroke(2.dp, Color.Gray),
            ) {
                Icon(Icons.Default.List, contentDescription = "Shopping List")
            }
//            Spacer(modifier = Modifier.padding(10.dp))
//            Button(onClick = { setShowSheet(OpenSheet.AddMeal)},
//                shape = CircleShape,
//                modifier = Modifier.size(80.dp),
//                border = BorderStroke(2.dp, Color.Gray),
//            ) {
//                Icon(Icons.Default.Add, contentDescription = "Add Recipe")
//            }
        }

    }

}
