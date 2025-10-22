package com.instamealmobile.ui.pages

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.viewModels.MenuViewModel
import com.instamealmobile.R
import com.instamealmobile.viewModels.NavViewModel

@Composable
fun Star(index: Int, rating: MutableState<Float>) {
    Box {
        Icon(
            painter = if (index <= rating.value) {
                painterResource(id = R.drawable.baseline_star_24)
            } else if ((index - .5).toFloat() == rating.value) {
                painterResource(id = R.drawable.baseline_star_half_24)
            } else {
                painterResource(id = R.drawable.baseline_star_outline_24)
            },
            contentDescription = "Star",
            modifier = Modifier.size(24.dp).clickable { rating.value = index.toFloat()}
        )
    }
}

@Composable
fun RatingAlert(recipeId: String) {
    val viewModel : MenuViewModel = viewModel()
    val nav : NavViewModel = viewModel()
    var rating = remember { mutableFloatStateOf(4F) }
    val context = LocalContext.current
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
                Text("How was the meal?")
                Row {
                    Star(1,rating)
                    Star(2,rating)
                    Star(3,rating)
                    Star(4,rating)
                    Star(5,rating)
                }
                Row(modifier = Modifier.padding(top = 15.dp)) {
                    Button({
                        viewModel.finishMeal(recipeId,rating.floatValue)
                        Toast.makeText(context, "Recipe Finished", Toast.LENGTH_SHORT).show()
                        nav.closeAlert()
                    }, modifier = Modifier.padding(end=5.dp)) {
                        Text("Save Rating")
                    }
                    OutlinedButton(
                        colors = ButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            containerColor = Color.Transparent,
                            disabledContentColor = MaterialTheme.colorScheme.onBackground,
                            disabledContainerColor = Color.Transparent
                        ),
                        onClick = {
                        viewModel.finishMeal(recipeId, null)
                        Toast.makeText(context, "Recipe Finished", Toast.LENGTH_SHORT).show()
                        nav.closeAlert()
                    }) {
                        Text("Skip")
                    }

                }

            }

        }
    }
}