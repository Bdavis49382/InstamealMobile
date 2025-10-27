package com.instamealmobile.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.viewModels.NavViewModel

@Composable
fun DiscardAlert() {
    val nav : NavViewModel = viewModel()
    Dialog(onDismissRequest = nav::closeAlert) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(0.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()) {
                Text("Leaving this page will discard your recipe in progress.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(.8f))
                Row(modifier = Modifier.padding(top = 15.dp)) {
                    Button({
                        nav.closeAlert()
                    }, modifier = Modifier.padding(end=10.dp)) {
                        Text("Stay")
                    }
                    OutlinedButton(
                        colors = ButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            containerColor = Color.Transparent,
                            disabledContentColor = MaterialTheme.colorScheme.onBackground,
                            disabledContainerColor = Color.Transparent
                        ),
                        onClick = {
                            nav.closeSheet()
                            nav.closeAlert()
                        }) {
                        Text("Discard")
                    }

                }

            }

        }
    }
}