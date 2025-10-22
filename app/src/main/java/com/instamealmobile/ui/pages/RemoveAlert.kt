package com.instamealmobile.ui.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.viewModels.MenuViewModel
import com.instamealmobile.viewModels.NavViewModel


@Composable
fun RemoveAlert(recipeId: String) {
    val viewModel : MenuViewModel = viewModel()
    val nav : NavViewModel = viewModel()
    val context = LocalContext.current
    Dialog(onDismissRequest = nav::closeAlert) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(0.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Are you sure you would like to remove this meal from your menu? It won't be deleted, just lose its spot in your menu!",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(.9f))
                OutlinedButton(

                    colors = ButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = Color.Transparent,
                        disabledContentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContainerColor = Color.Transparent
                    ),
                    onClick={
                    viewModel.removeMenuItem(recipeId)
                    Toast.makeText(context, "Meal Removed", Toast.LENGTH_SHORT).show()
                    nav.closeAlert()
                }) {
                    Text("Remove")
                }
            }
        }
    }
}
