package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun JoinHousehold(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(0.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
           Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.padding(30.dp)) {
               Text("Join by entering the code provided by the head of your household.", textAlign = TextAlign.Center)
               TextField(
                   value = "",
                   onValueChange = {},
                   singleLine = true,
               )
               Button({}) {
                   Text("Submit")
               }
           }
        }
    }
}