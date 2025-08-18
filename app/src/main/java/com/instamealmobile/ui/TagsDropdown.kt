package com.instamealmobile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.data.ApiState
import com.instamealmobile.viewModels.AddRecipeToFeedViewModel
import com.instamealmobile.viewModels.SearchBarViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagsDropdown() {
    val viewModel : AddRecipeToFeedViewModel = viewModel()
    val searchBarViewModel : SearchBarViewModel = viewModel()
    var extended by remember { mutableStateOf(false) }
    val tagsState by searchBarViewModel.tags.collectAsState(ApiState.Loading)
    val focusManager = LocalFocusManager.current

    val filteredTags by remember { derivedStateOf {
        if (tagsState is ApiState.Success) {
            (tagsState as ApiState.Success<List<String>>).data.filter {
                (it.uppercase().contains(viewModel.newTag.uppercase()) || viewModel.newTag.isEmpty())
                    && it != "MyRecipes"
                    && !viewModel.tags.contains(it)

            }.plus("Favorites".takeIf {viewModel.newTag.isEmpty() }.orEmpty()).toSet().toList().sorted()
        } else {
            listOf()
        }
    } }

    Column {
        OutlinedTextField(
            value = viewModel.newTag,
            placeholder = {Text("New Tag")},
            onValueChange = {viewModel.newTag = it
                            extended = true},
            isError = viewModel.validatorsActive.value && viewModel.tags.isEmpty(),
            supportingText = {
                if (viewModel.validatorsActive.value && viewModel.tags.isEmpty()) {
                    Text("At least one tag is required, so that you can easily find your recipes.")
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.addTag(viewModel.newTag)
                    viewModel.newTag = ""
                    extended = true
                }
            ),
            singleLine = true,
            textStyle = TextStyle(fontSize = 12.sp),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .onFocusChanged { focusState ->
                    extended = focusState.isFocused
                }
                .width(150.dp)
                .padding(end = 5.dp, bottom = 5.dp)
        )
        DropdownMenu(expanded = extended,
            onDismissRequest = {extended = false},
            modifier = Modifier.height(250.dp),
            properties = PopupProperties(focusable = false)
            ) {
            filteredTags.forEach {
                    DropdownMenuItem(
                        text={Text(it)},
                        onClick = {
                            extended = false
                            focusManager.clearFocus()
                            viewModel.addTag(it)
                            viewModel.newTag = ""
                        }
                    )
                }

        }
    }

}