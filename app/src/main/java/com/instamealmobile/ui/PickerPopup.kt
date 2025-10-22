package com.instamealmobile.ui

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.OpenAlert
import com.instamealmobile.R
import com.instamealmobile.viewModels.AddRecipeToFeedViewModel
import com.instamealmobile.viewModels.NavViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

enum class ImagePurpose {
    TextParsing, ImageStoring, TextParsingIngredients, TextParsingSteps
}

fun showImportMessage(scope: CoroutineScope, snackbarHostState: SnackbarHostState, context: Context) {
    scope.launch {
        snackbarHostState.showSnackbar(message = context.getString(R.string.importMessage),
            withDismissAction = true,
            duration = SnackbarDuration.Indefinite)
    }

}

@Composable
fun PickerPopup(popupIsOn: Boolean,imagePurpose: ImagePurpose, snackbarHostState: SnackbarHostState,scope: CoroutineScope, onDismiss: () -> Unit) {
    val viewModel : AddRecipeToFeedViewModel = viewModel()
    val navViewModel : NavViewModel = viewModel()
    val context = LocalContext.current

    val pdfPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            viewModel.parsePdf(uri, context) {
                showImportMessage(scope, snackbarHostState, context)
            }
        }
    }

    val pickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            if (imagePurpose == ImagePurpose.ImageStoring) {
                viewModel.uploadImage(it, context) {
                    Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                viewModel.parseText(it, context, imagePurpose) {
                    showImportMessage(scope, snackbarHostState, context)
                }
            }
        }
    }

    val cameraUri = remember { mutableStateOf<Uri?>(null) }
    val cameraFile = File(context.cacheDir, "captured_image.jpg")
    cameraUri.value = FileProvider.getUriForFile(context, "${context.packageName}.provider", cameraFile)

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && cameraUri.value != null) {
            if (imagePurpose == ImagePurpose.ImageStoring) {
                viewModel.uploadImage(cameraUri.value!!, context) {
                    Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_LONG).show()
                }
            } else {
                viewModel.parseText(cameraUri.value!!, context, imagePurpose) { recipe ->
                    showImportMessage(scope, snackbarHostState, context)
                }
            }
        }
    }

    if (popupIsOn) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (imagePurpose == ImagePurpose.ImageStoring) {
                        Text("Upload Image", modifier = Modifier.padding(bottom = 10.dp))
                    } else {
                        Text("Import Recipe", modifier = Modifier.padding(bottom = 10.dp))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                        ) {
                        OutlinedButton({
                            cameraLauncher.launch(cameraUri.value!!)
                            onDismiss()
                        }) {
                            Icon(painter = painterResource(R.drawable.baseline_photo_camera_24),"camera")
                        }
                        OutlinedButton({
                            pickerLauncher.launch("image/*")
                            onDismiss()
                        }) {
                            Icon(painter = painterResource(R.drawable.baseline_image_search_24),"image search")
                        }
                        if (imagePurpose == ImagePurpose.TextParsing) {
                            OutlinedButton({
                                pdfPickerLauncher.launch(arrayOf("application/pdf"))
                                onDismiss()
                            }) {
                                Icon(painter = painterResource(R.drawable.baseline_picture_as_pdf_24),"pdf search")
                            }
                            OutlinedButton({
                                navViewModel.openAlert = OpenAlert.Link
                                onDismiss()
                            }) {
                                Icon(painter = painterResource(R.drawable.baseline_insert_link_24),"import from link")
                            }
                        }
                    }
                }
            }
        }
    }
}