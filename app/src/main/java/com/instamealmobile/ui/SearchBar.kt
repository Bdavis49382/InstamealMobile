package com.instamealmobile.ui

import Tag
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.OpenSheet
import com.instamealmobile.R
import com.instamealmobile.data.ApiState
import com.instamealmobile.viewModels.FeedViewModel
import com.instamealmobile.viewModels.NavViewModel
import com.instamealmobile.viewModels.SearchBarViewModel

@Composable
fun SearchBar() {
    var searchBoxText by remember { mutableStateOf("") }
    val nav: NavViewModel = viewModel()
    val viewModel: FeedViewModel = viewModel()
    val searchBarViewModel: SearchBarViewModel = viewModel()
    val tagsState by searchBarViewModel.tags.collectAsState(ApiState.Loading)
    var activated by remember { mutableStateOf(false)}
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var textFieldValueState by remember { mutableStateOf(
        TextFieldValue(text="", selection = TextRange.Zero)
    ) }

    LaunchedEffect(Unit) {
        searchBarViewModel.getTags()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End) {
            if (activated) {
                when (tagsState) {
                    is ApiState.Success<*> -> {
                        val tags = (tagsState as ApiState.Success).data
                        Box(contentAlignment = Alignment.BottomEnd,modifier = Modifier.height(180.dp).width(300.dp).padding(10.dp)) {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(3.dp), horizontalAlignment = Alignment.End) {
                                items(tags) {
                                    Tag(it) {
                                        if (searchBoxText.contains("#$it ")) {
                                            searchBoxText = searchBoxText.replace("#$it ","")
                                        } else {
                                            searchBoxText += "#$it "
                                        }
                                        textFieldValueState = textFieldValueState.copy(
                                            text = searchBoxText,
                                            selection = TextRange(searchBoxText.length)
                                        )
                                    }
                                }
                            }

                        }

                    }
                    else -> {

                    }

                }
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 60.dp, end = 5.dp, start = 5.dp)
                ,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextField(
                    value = textFieldValueState,
                    onValueChange = {searchBoxText = it.text
                        textFieldValueState = it
                                    },
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
                        activated = false
                        searchBoxText = ""
                        textFieldValueState = textFieldValueState.copy(
                            text = searchBoxText
                        )
                    }),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 20.sp),
                    modifier = Modifier
                        .fillMaxWidth(.70f)
                        .onFocusChanged({ focusState ->
                            activated = focusState.isFocused
                        })
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
}