package com.instamealmobile.ui.pages

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.OpenSheet
import com.instamealmobile.data.RecipeIdentifier
import com.instamealmobile.ui.DeleteButton
import com.instamealmobile.ui.EditableText
import com.instamealmobile.ui.EditableTextState
import com.instamealmobile.ui.ImageBox
import com.instamealmobile.ui.ImagePurpose
import com.instamealmobile.ui.PickerPopup
import com.instamealmobile.ui.SideButton
import com.instamealmobile.ui.SideButtons
import com.instamealmobile.ui.TagsDropdown
import com.instamealmobile.viewModels.AddRecipeToFeedViewModel
import com.instamealmobile.viewModels.MenuViewModel
import com.instamealmobile.viewModels.NavViewModel
import com.instamealmobile.viewModels.Purpose
import com.instamealmobile.viewModels.SearchBarViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState


@Composable
fun AddRecipeToFeed(lazyListState: LazyListState) {
    val viewModel: AddRecipeToFeedViewModel =  viewModel()
    val menuViewModel: MenuViewModel = viewModel()
    val nav: NavViewModel = viewModel()
    val searchBarViewModel: SearchBarViewModel = viewModel()
    val context = LocalContext.current
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) {from, to ->
        val itemsPrevious = 2 + viewModel.tags.size + 6
        val stepsItemsPrevious = itemsPrevious + viewModel.ingredients.size + 2
        if (from.index - itemsPrevious < viewModel.ingredients.size) {
            // If both from and to are ingredients
            if (to.index - itemsPrevious < viewModel.ingredients.size) {
                viewModel.ingredients.add(to.index - itemsPrevious, viewModel.ingredients.removeAt(from.index - itemsPrevious))
            } else { // If from is ingredients and to is steps
                viewModel.steps.add(to.index - stepsItemsPrevious, viewModel.ingredients.removeAt(from.index - itemsPrevious))
            }
        } else {
            // If both from and to are steps
            if (to.index - itemsPrevious > viewModel.ingredients.size) {
                viewModel.steps.add(to.index - stepsItemsPrevious, viewModel.steps.removeAt(from.index - stepsItemsPrevious))
            } else { // If from is steps and to is ingredients
                viewModel.ingredients.add(to.index - itemsPrevious, viewModel.steps.removeAt(from.index - stepsItemsPrevious))
            }
        }
    }

    var imgPurpose by remember {mutableStateOf(ImagePurpose.ImageStoring)}
    var popupIsOn by remember {mutableStateOf(false)}
    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        viewModel.setRecipe(nav.getRecipe())
    }

    PickerPopup(popupIsOn, imgPurpose) {popupIsOn = false }

    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth(.9f)
        ) {
            item {
                EditableTextState(
                    text = viewModel.title,
                    placeholder = "Title",
                    onSubmit = {
                        viewModel.title.value = it
                    },
                    maxLines = 2,
                    errorCondition = viewModel.validatorsActive.value && viewModel.title.value.isEmpty(),
                    errorMessage = "A title is required.",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )
            }
            item {
                TagsDropdown()
            }
            itemsIndexed(viewModel.tags, key= {index,item -> "$item$index,tags"}) { index,item ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(50.dp).padding(start=if (viewModel.tagsExtended) 100.dp else 0.dp)) {
                    DeleteButton {
                        viewModel.tags.removeAt(index)
                    }
                    EditableText(text = item, maxLines = 4, placeholder = "MainDish", modifier = Modifier
                        .padding(start = 2.dp, bottom = 5.dp, end = 8.dp)
                    ) {
                        viewModel.tags[index] = it
                    }
                }
            }
            item {
                Row {
                    EditableTextState(
                        text = viewModel.source,
                        placeholder = "Source",
                        onSubmit = {
                            viewModel.source.value = it
                        },
                        maxLines = 1,
                        fontSize = 12.sp,
                        modifier = Modifier
                    )
                }
            }
            item {
                ImageBox {
                    imgPurpose = ImagePurpose.ImageStoring
                    popupIsOn = true
                }
            }
            item {
                Row(modifier = Modifier.padding(top = 5.dp)) {
                    EditableTextState(
                        text = viewModel.servings,
                        placeholder = "Servings",
                        precursor = "Servings: ",
                        onSubmit = {
                            viewModel.servings.value = it
                        },
                        maxLines = 1,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .width(180.dp)
                            .padding(end = 10.dp)
                    )
                    EditableTextState(
                        text = viewModel.totalTime,
                        placeholder = "Total Time",
                        precursor = "Total Time: ",
                        onSubmit = {
                            viewModel.totalTime.value = it
                        },
                        maxLines = 1,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .width(180.dp)
                    )
                }
            }
            item {
                Button({
                    imgPurpose = ImagePurpose.TextParsing
                    popupIsOn = true
                }, modifier = Modifier.focusProperties { canFocus = false}) {
                    Text("Import Recipe")
                }
            }
            item {
                Row(modifier = Modifier.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Ingredients",
                        style = TextStyle(fontSize = 25.sp),
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Button({
                        imgPurpose = ImagePurpose.TextParsingIngredients
                        popupIsOn = true
                    }, modifier = Modifier.focusProperties { canFocus = false}) {
                        Text("Import")
                    }
                }
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
            itemsIndexed(viewModel.ingredients, key= {index,item -> item}) { index,item ->
                ReorderableItem(reorderableLazyListState, key = item) { isDragging ->
                    val shadowElevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .shadow(shadowElevation).fillMaxWidth()) {
                        DeleteButton{
                            viewModel.ingredients.removeAt(index)
                        }
                        EditableText(text = item, maxLines = 4, placeholder = "Ingredient", modifier = Modifier
                            .width(300.dp)
                            .padding(start = 5.dp, bottom = 5.dp)
                        ) {
                            viewModel.ingredients[index] = it
                        }
                        IconButton({}, modifier = Modifier.draggableHandle()) {
                            Icon(Icons.Rounded.Menu, contentDescription = "Reorder")
                        }
                    }
                }
            }
            item {
                Row(modifier = Modifier.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text="Steps",
                        style = TextStyle(fontSize = 25.sp),
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Button({
                        imgPurpose = ImagePurpose.TextParsingSteps
                        popupIsOn = true
                    }, modifier = Modifier.focusProperties { canFocus = false}) {
                        Text("Import")
                    }
                }
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
                        .width(240.dp)
                        .padding(end = 5.dp, bottom = 5.dp)
                )

            }
            itemsIndexed(viewModel.steps, key={index,item -> item}) { index,item ->
                ReorderableItem(reorderableLazyListState, key = item) { isDragging ->
                    val shadowElevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                    Row(modifier = Modifier.shadow(shadowElevation).fillMaxWidth()) {
                        DeleteButton {
                            viewModel.steps.removeAt(index)
                        }
                        EditableText(
                            text = item, placeholder = "Step", maxLines = 50, modifier = Modifier
                                .width(300.dp)
                                .padding(start = 5.dp, bottom = 5.dp)
                        ) {
                            viewModel.steps[index] = it
                        }
                        IconButton({}, modifier = Modifier.draggableHandle()) {
                            Icon(Icons.Rounded.Menu, contentDescription = "Reorder")
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
        SideButtons {
            SideButton({
                // This ensures that any changes made to list items are saved.
                focusManager.clearFocus(force = true)
                val validSubmission = viewModel.submitRecipe(viewModel.fullRecipe.value.id) { recipe ->
                    searchBarViewModel.getTags()
                    nav.pickedRecipe = RecipeIdentifier.factory(recipe)
                    if (nav.addToFeedPurpose == Purpose.AddNew || nav.addToFeedPurpose == Purpose.FeedEdit) {
                        nav.openSheet = OpenSheet.PreviewRecipe
                        if (nav.addToFeedPurpose == Purpose.AddNew) {
                            Toast.makeText(
                                context,
                                "Recipe Added to My Recipes",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else if (nav.addToFeedPurpose == Purpose.MenuEdit) {
                        nav.navigateTo(OpenSheet.ViewRecipe, recipe = nav.pickedRecipe)
                        menuViewModel.getMenu(allowCache = false)
                        Toast.makeText(context, "Recipe Updated", Toast.LENGTH_SHORT).show()
                    }
                }
                if (!validSubmission) {
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
