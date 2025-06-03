package com.instamealmobile.ui

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.instamealmobile.R
import com.instamealmobile.viewModels.AddRecipeToFeedViewModel
import java.io.File

enum class ImagePurpose {
    TextParsing, ImageStoring
}

@Composable
fun PickerPopup(popupIsOn: Boolean,imagePurpose: ImagePurpose, onDismiss: () -> Unit) {
    val viewModel : AddRecipeToFeedViewModel = viewModel()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.uploadImage(it, context) {
            Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
        } }
    }

    val textLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.parseText(it, context) {
            Toast.makeText(context, "Grabbed Recipe From Image - Check For Accuracy", Toast.LENGTH_SHORT).show()
        }}
    }

    val cameraUri = remember { mutableStateOf<Uri?>(null) }
    val cameraFile = File(context.cacheDir, "captured_image.jpg")
    cameraUri.value = FileProvider.getUriForFile(context, "${context.packageName}.provider", cameraFile)
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            success ->
        if (success && cameraUri.value != null) {
            viewModel.uploadImage(cameraUri.value!!, context) {
                Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_LONG).show()
            }
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
    if (popupIsOn) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .width(220.dp)
                    .height(100.dp)
                    .padding(0.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("Upload Image", modifier = Modifier.padding(bottom = 10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        ) {
                        OutlinedButton({
                            if (imagePurpose == ImagePurpose.ImageStoring) {
                                cameraLauncher.launch(cameraUri.value!!)
                            } else {
                                textCameraLauncher.launch(cameraUri.value!!)
                            }
                            onDismiss()
                        }, modifier = Modifier.padding(end = 15.dp)) {
                            Icon(painter = painterResource(R.drawable.baseline_photo_camera_24),"camera")
                        }
                        OutlinedButton({
                            if (imagePurpose == ImagePurpose.ImageStoring)
                                launcher.launch("image/*")
                            else
                                textLauncher.launch("image/*")
                            onDismiss()
                        }) {
                            Icon(painter = painterResource(R.drawable.baseline_image_search_24),"image search")
                        }
                    }
                }

            }
        }
    }
}