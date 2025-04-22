package com.instamealmobile.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instamealmobile.makeLongList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipePage(onDismiss : () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    var searchBoxText by remember { mutableStateOf("") }
    var alertOpen by remember { mutableStateOf(false) }
    var pickedRecipe by remember { mutableStateOf("") }
    val height = 350.dp
//    if (alertOpen) {
//        ItemConfirmationDialog(pickedRecipe = pickedRecipe) { alertOpen = false}
//    }

    ModalBottomSheet(onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle()}
    ) {
        Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                TextField(
                    value = searchBoxText,
                    onValueChange = {searchBoxText = it},
                    label = { Text("Search") },
                    placeholder = { Text("Search Powered by GPT...") },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black, fontSize = 20.sp)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .height(height)
                    .fillMaxWidth()
            ) {
                items(makeLongList()) { meal ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp)
                            .clickable {
                                pickedRecipe = meal
                                alertOpen = true
                            }
                    ) {
                        Text(text=meal, fontSize = 25.sp, modifier = Modifier.padding(3.dp))
                    }
                }
            }

        }
    }
}
