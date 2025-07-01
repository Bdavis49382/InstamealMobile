package com.instamealmobile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.OpenSheet
import com.instamealmobile.R
import com.instamealmobile.viewModels.FeedViewModel
import com.instamealmobile.viewModels.NavViewModel

@Composable
fun SearchBar(viewModel:  FeedViewModel) {
    var searchBoxText by remember { mutableStateOf("") }
    val nav: NavViewModel = viewModel()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 60.dp, end = 5.dp, start = 5.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            ) {
            TextField(
                value = searchBoxText,
                onValueChange = {searchBoxText = it},
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                leadingIcon = { Icon(Icons.Default.Search, "Search Icon") },
                placeholder = { Text("Search Recipes") },
                keyboardOptions = KeyboardOptions(imeAction= ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.searchFeed(searchBoxText)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }),
                singleLine = true,
                textStyle = TextStyle(fontSize = 20.sp),
                modifier = Modifier
                    .fillMaxWidth(.70f)
                    .clip(RoundedCornerShape(20.dp))
            )
            Button(modifier = Modifier.size(90.dp).clip(CircleShape),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimary),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                onClick = {
                    nav.navigateTo(OpenSheet.ShoppingList)
                }) {
                Icon(painter = painterResource(R.drawable.shoppinglisticon), contentDescription = "Shopping List")
            }

        }
    }
}