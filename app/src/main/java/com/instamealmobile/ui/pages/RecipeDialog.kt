package com.instamealmobile.ui.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.instamealmobile.makeLongList
import com.instamealmobile.ui.CheckIngredient
import com.instamealmobile.ui.CheckItem
import java.nio.file.WatchEvent

@Composable
fun RecipeDialog(pickedRecipe : String, onDismiss: () -> Unit) {
    val recipeParts = mutableListOf("Ingredients:")
    recipeParts.addAll(makeLongList(10))
    recipeParts.add("\nSteps:")
    recipeParts.add(makeLongList(20).joinToString())
    recipeParts.add(makeLongList(10).joinToString())
    recipeParts.add(makeLongList(10).joinToString())
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(0.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = onDismiss) {
                        Icon(Icons.Default.Check,contentDescription = "Confirm")
                    }
                }
            ) {innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = pickedRecipe,
                        fontSize = 30.sp
                    )
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(450.dp)
                                .padding(5.dp)
                        ) {
                            items(recipeParts) {item ->
                                Text(item, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                }

            }
        }
    }

}
