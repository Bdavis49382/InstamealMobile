package com.instamealmobile.ui.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.viewModels.HouseholdViewModel

@Composable
fun JoinHousehold(reload: () -> Unit,onDismiss: () -> Unit) {
    val viewModel: HouseholdViewModel =  viewModel()
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(0.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
           Column(horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.spacedBy(10.dp) ,
               modifier = Modifier.padding(30.dp)) {
               Text("Join by entering the code provided by the head of your household.", textAlign = TextAlign.Center)
               TextField(
                   value = viewModel.codeEntry,
                   onValueChange = {viewModel.codeEntry = it},
                   singleLine = true,
               )
               Button({viewModel.joinHousehold(reload)
                   Toast.makeText(context, "Joined Household", Toast.LENGTH_SHORT).show()
                   onDismiss()}) {
                   Text("Submit")
               }
           }
        }
    }
}