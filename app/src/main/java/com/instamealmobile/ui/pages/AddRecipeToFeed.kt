package com.instamealmobile.ui.pages

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.instamealmobile.R
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.ui.EditableText
import com.instamealmobile.ui.EditableTextState
import com.instamealmobile.ui.ImagePurpose
import com.instamealmobile.ui.PickerPopup
import com.instamealmobile.ui.SideButton
import com.instamealmobile.ui.SideButtons
import com.instamealmobile.viewModels.AddRecipeToFeedViewModel


@Composable
fun AddRecipeToFeed(recipe: Recipe,lazyListState: LazyListState,confirm: (Recipe) -> Unit) {
    val viewModel: AddRecipeToFeedViewModel =  viewModel()
    val imgLinkState by viewModel.img_link.collectAsState()
    val context = LocalContext.current

    var imgPurpose by remember {mutableStateOf(ImagePurpose.ImageStoring)}
    var popupIsOn by remember {mutableStateOf(false)}
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        viewModel.setRecipe(recipe)
    }

    PickerPopup(popupIsOn, imgPurpose) {popupIsOn = false }

    Box(contentAlignment = Alignment.TopStart, modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .padding(20.dp)
        ) {
            Row(modifier = Modifier.height(200.dp)) {
                Column(modifier = Modifier.width(200.dp).fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                    EditableTextState(
                        text = viewModel.title,
                        placeholder = "title",
                        onSubmit = {
                            viewModel.title.value = it
                            focusManager.moveFocus(FocusDirection.Down)
                           },
                        maxLines = 2,
                        errorCondition = viewModel.validatorsActive.value && viewModel.title.value.isEmpty(),
                        errorMessage = "A title is required.",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                    )
                    EditableText(
                        text = viewModel.source,
                        placeholder = "Source",
                        onSubmit = {viewModel.source = it
                            focusManager.moveFocus(FocusDirection.Down)
                                   },
                        maxLines = 1,
                        fontSize = 12.sp,
                        modifier = Modifier
                    )
                }
                Box(modifier = Modifier.width(400.dp).focusProperties {canFocus = false}
                    , contentAlignment = Alignment.Center) {
                    Crossfade(targetState = imgLinkState, label = "ContentSwitch") { screenState ->
                        when (screenState) {
                            is ApiState.Loading -> {
                                CircularProgressIndicator()
                            }

                            is ApiState.Success -> if (imgLinkState is ApiState.Success){
                                val img_link = (imgLinkState as ApiState.Success<String>).data
                                AsyncImage(
                                    model = img_link,
                                    modifier = Modifier
                                        .clickable {
                                            imgPurpose = ImagePurpose.ImageStoring
                                            popupIsOn = true
                                        }
                                        .clip(RoundedCornerShape(10.dp)),
                                    contentDescription = null
                                )

                            }

                            is ApiState.Error -> if (imgLinkState is ApiState.Error) {
                                val error = (imgLinkState as ApiState.Error).message
                                Text(error)
                            }

                            is ApiState.Resting -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(5.dp).fillMaxSize()
                                ) {
                                    OutlinedButton({
                                        imgPurpose = ImagePurpose.ImageStoring
                                        popupIsOn = true
                                    }) {
                                        Icon(
                                            painter = painterResource(R.drawable.baseline_add_a_photo_24),
                                            contentDescription = "Add Photo"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Button({
                imgPurpose = ImagePurpose.TextParsing
                popupIsOn = true
            }, modifier = Modifier.focusProperties { canFocus = false}) {
                Text("Grab Recipe From Image")
            }
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    EditableTextState(
                        text = viewModel.servings,
                        placeholder = "Servings",
                        precursor = "Servings: ",
                        onSubmit = {
                            viewModel.servings.value = it
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                        maxLines = 1,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                }
                item {
                    EditableTextState(
                        text = viewModel.totalTime,
                        placeholder = "Total Time",
                        precursor = "Total Time: ",
                        onSubmit = {
                            viewModel.totalTime.value = it
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                        maxLines = 1,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                    )
                }
                item {
                    Text(
                        text = "Ingredients",
                        style = TextStyle(fontSize = 25.sp),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)
                    )

                }
                item {
                    OutlinedTextField(
                        value = viewModel.newIngredient,
                        placeholder = {Text("New Ingredient")},
                        onValueChange = {viewModel.newIngredient = it},
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        isError = viewModel.validatorsActive.value && viewModel.ingredients.isEmpty(),
                        supportingText = {
                            if (viewModel.validatorsActive.value && viewModel.ingredients.isEmpty()) {
                                Text("At least one ingredient is required.")
                            }
                        },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.ingredients.add(viewModel.newIngredient)
                                viewModel.newIngredient = ""
                            }
                        ),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 12.sp),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(270.dp)
                            .padding(end = 5.dp, bottom = 5.dp)
                    )

                }
                itemsIndexed(viewModel.ingredients) { index,item ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        EditableText(text = item, maxLines = 4, placeholder = "Ingredient", modifier = Modifier
                            .width(270.dp)
                            .padding(end = 5.dp, bottom = 5.dp)
                        ) {
                            viewModel.ingredients[index] = it
                        }
                        Button({viewModel.ingredients.removeAt(index)}, shape = CircleShape, modifier = Modifier.padding(horizontal = 5.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove")
                        }
                    }
                }
                item {
                    Text(text="Steps",
                        style = TextStyle(fontSize = 25.sp),
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = viewModel.newStep,
                        placeholder = {Text("New Step")},
                        isError = viewModel.validatorsActive.value && viewModel.steps.isEmpty(),
                        supportingText = {
                            if (viewModel.validatorsActive.value && viewModel.steps.isEmpty()) {
                                Text("At least one step is required.")
                            }
                        },
                        onValueChange = {viewModel.newStep = it},
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.steps.add(viewModel.newStep)
                                viewModel.newStep = ""
                            }
                        ),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 12.sp),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .width(270.dp)
                            .padding(end = 5.dp, bottom = 5.dp)
                    )

                }
                itemsIndexed(viewModel.steps) { index,item ->
                    Row {
                        EditableText(text = item, placeholder = "Step", maxLines = 50, modifier = Modifier
                            .width(270.dp)
                            .padding(end = 5.dp, bottom = 5.dp)
                        ) {
                            viewModel.steps[index] = it
                        }
                        Button({viewModel.steps.removeAt(index)}, shape = CircleShape) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove")
                        }
                    }
                }
            }

        }
        SideButtons {
            SideButton({
                if (!viewModel.submitRecipe(recipe.id,confirm)) {
                    viewModel.validatorsActive.value = true
                    Toast.makeText(context,"More Details Are Needed to Save Recipe", Toast.LENGTH_SHORT).show()
                }
            }
            ) {
                Text("Save")
            }
        }
    }
}
