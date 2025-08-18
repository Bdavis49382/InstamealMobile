package com.instamealmobile.ui.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.viewModels.AddRecipeToFeedViewModel
import com.instamealmobile.viewModels.NavViewModel

@Composable
fun GetRecipeByLink() {
    val viewModel: AddRecipeToFeedViewModel =  viewModel()
    val nav: NavViewModel = viewModel()
    val context = LocalContext.current
    Dialog(onDismissRequest = nav::closeAlert) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
           Column(horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.spacedBy(10.dp) ,
               modifier = Modifier.padding(30.dp)) {
               Text("Enter Link to Recipe", textAlign = TextAlign.Center)
               TextField(
                   value = viewModel.src_link.value,
                   onValueChange = {viewModel.src_link.value = it},
                   singleLine = true,
               )
               Button({
                   if (viewModel.src_link.value.isBlank()) {
                       Toast.makeText(context, "Please Enter Link", Toast.LENGTH_SHORT).show()
                   } else {
                       Toast.makeText(context, "Entering information...", Toast.LENGTH_LONG).show()
                       viewModel.parseWebsite( {
                           Toast.makeText(context, "Recipe imported.", Toast.LENGTH_SHORT).show()
                           nav.closeAlert()
                       }, {
                           Toast.makeText(context, "The provided link was invalid.", Toast.LENGTH_SHORT).show()

                       }) {
                           Toast.makeText(context, "Website did not support importing. Take a screenshot and import as an image.", Toast.LENGTH_SHORT).show()
                          }
                   }
               })
               {
                   Text("Submit")
               }
               Text("Tip: Try importing straight from your browser! Hit share on a recipe you love and select the Easy Meals logo from the list!", fontSize = 12.sp)
           }
        }
    }
}