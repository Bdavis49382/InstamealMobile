package com.instamealmobile.ui.pages

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.instamealmobile.data.ApiState
import com.instamealmobile.data.Recipe
import com.instamealmobile.ui.EditableText
import com.instamealmobile.ui.EditableTextState
import com.instamealmobile.viewModels.AddRecipeToFeedViewModel
import java.io.File


@Composable
fun AddRecipeToFeed(confirm: (Recipe) -> Unit) {
    val viewModel: AddRecipeToFeedViewModel =  viewModel()

    val imgLinkState by viewModel.img_link.observeAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.uploadImage(it, context)}
        Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
    }

    val cameraUri = remember { mutableStateOf<Uri?>(null) }
    val cameraFile = File(context.cacheDir, "captured_image.jpg")
    cameraUri.value = FileProvider.getUriForFile(context, "${context.packageName}.provider", cameraFile)
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        success ->
        if (success && cameraUri.value != null) {
            viewModel.uploadImage(cameraUri.value!!, context)
            Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_LONG).show()
        }
    }
    val textCameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            success ->
        if (success && cameraUri.value != null) {
            viewModel.parseText(cameraUri.value!!, context) { recipe ->
                Log.i("FULL_TEXT",recipe)
                Toast.makeText(context, "Grabbed Recipe From Image - Check For Accuracy", Toast.LENGTH_LONG).show()
            }
        }
    }

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
                        maxLines = 1,
                        fontSize = 13.sp,
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
//                        Upload Image File
                    when (imgLinkState) {
                        is ApiState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is ApiState.Success -> {
                            val img_link =(imgLinkState as ApiState.Success<String>).data
                            AsyncImage(
                                model = img_link,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp)),
                                contentDescription = null
                            )

                        }
                        is ApiState.Error -> {
                            val error = (imgLinkState as ApiState.Error).message
                            Text(error)
                        }
                        null -> {
                            Column(modifier = Modifier.padding(5.dp)) {
                                Button({ launcher.launch("image/*")}) {
                                    Text("Upload Photo For Recipe")
                                }
                                Button({ cameraLauncher.launch(cameraUri.value!!)}) {
                                    Text("Take Picture For Recipe")
                                }
                            }
                        }
                    }
                }
            }
            Button({ textCameraLauncher.launch(cameraUri.value!!)}) {
                Text("Take Picture of Recipe Text")
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Servings:")
                        EditableTextState(
                            text = viewModel.servings,
                            placeholder = "Servings",
                            onSubmit = {viewModel.servings.value = it},
                            maxLines = 1,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Total Time:")
                        EditableTextState(
                            text = viewModel.totalTime,
                            placeholder = "Total Time",
                            onSubmit = {viewModel.totalTime.value = it},
                            maxLines = 1,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                        )
                    }
                }
                item {
                    Text(
                        text = "Ingredients",
                        style = TextStyle(fontSize = 25.sp),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)
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
                    TextField(
                        value = viewModel.newIngredient,
                        placeholder = {Text("Ingredient")},
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
                item {
                    Text(text="Steps",
                        style = TextStyle(fontSize = 25.sp),
                        modifier = Modifier.padding(vertical = 5.dp)
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
                item {
                    TextField(
                        value = viewModel.newStep,
                        placeholder = {Text("Step")},
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
            }

        }
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 100.dp, horizontal = 20.dp)
        ) {
            Button({
                viewModel.submitRecipe(confirm) }, shape = RoundedCornerShape(10.dp), modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 5.dp)
            ) {
                Text("Save")
            }
        }
    }
}
