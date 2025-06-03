package com.instamealmobile.ui.pages

import android.widget.Toast
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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
import com.instamealmobile.viewModels.AddRecipeToFeedViewModel


@Composable
fun AddRecipeToFeed(recipe: Recipe,confirm: (Recipe) -> Unit) {
    val viewModel: AddRecipeToFeedViewModel =  viewModel()
    val imgLinkState by viewModel.img_link.observeAsState()
    val context = LocalContext.current

    var imgPurpose by remember {mutableStateOf(ImagePurpose.ImageStoring)}
    var popupIsOn by remember {mutableStateOf(false)}
    LaunchedEffect(Unit) {
        viewModel.setRecipe(recipe)
    }

    PickerPopup(popupIsOn, imgPurpose) {popupIsOn = false }

    Box(contentAlignment = Alignment.TopStart, modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .padding(20.dp)
        ) {
            Row(modifier = Modifier.height(150.dp)) {
                Column(modifier = Modifier.width(200.dp).fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                    EditableTextState(
                        text = viewModel.title,
                        placeholder = "title",
                        onSubmit = {viewModel.title.value = it},
                        maxLines = 2,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                    )
                    EditableText(
                        text = viewModel.source,
                        placeholder = "Source",
                        onSubmit = {viewModel.source = it},
                        maxLines = 1,
                        fontSize = 12.sp,
                        modifier = Modifier
                    )
                }
                Box(modifier = Modifier.width(400.dp), contentAlignment = Alignment.Center) {
                    when (imgLinkState) {
                        is ApiState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is ApiState.Success -> {
                            val img_link =(imgLinkState as ApiState.Success<String>).data
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
                        is ApiState.Error -> {
                            val error = (imgLinkState as ApiState.Error).message
                            Text(error)
                        }
                        is ApiState.Resting -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,modifier = Modifier.padding(5.dp).fillMaxSize()) {
                                OutlinedButton({
                                    imgPurpose = ImagePurpose.ImageStoring
                                    popupIsOn = true
                                }) {
                                    Icon(painter = painterResource(R.drawable.baseline_add_a_photo_24),contentDescription="Add Photo")
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }
            Button({
                imgPurpose = ImagePurpose.TextParsing
                popupIsOn = true
            }) {
                Text("Grab Recipe From Image")
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    EditableTextState(
                        text = viewModel.servings,
                        placeholder = "Servings",
                        precursor = "Servings: ",
                        onSubmit = {viewModel.servings.value = it},
                        maxLines = 1,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                    )
                    EditableTextState(
                        text = viewModel.totalTime,
                        placeholder = "Total Time",
                        precursor = "Total Time: ",
                        onSubmit = {viewModel.totalTime.value = it},
                        maxLines = 1,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
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
                    TextField(
                        value = viewModel.newIngredient,
                        placeholder = {Text("New Ingredient")},
                        onValueChange = {viewModel.newIngredient = it},
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                viewModel.ingredients.add(viewModel.newIngredient)
                                viewModel.newIngredient = ""
                            }
                        ),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 12.sp),
                        modifier = Modifier
                            .width(270.dp)
                            .padding(end = 5.dp, bottom = 5.dp)
                            .clip(RoundedCornerShape(20.dp))
                    )

                }
                itemsIndexed(viewModel.ingredients) { index,item ->
                    Row {
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
                    TextField(
                        value = viewModel.newStep,
                        placeholder = {Text("New Step")},
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
                        modifier = Modifier
                            .width(270.dp)
                            .padding(end = 5.dp, bottom = 5.dp)
                            .clip(RoundedCornerShape(20.dp))
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
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 200.dp)
        ) {
            SideButton({

                Toast.makeText(context, if (recipe.id == null)"Recipe Added to My Recipes" else "Updated Recipe", Toast.LENGTH_SHORT).show()
                viewModel.submitRecipe(recipe.id,confirm) }
            ) {
                Text("Save")
            }
        }
    }
}
