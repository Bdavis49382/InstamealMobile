package com.instamealmobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.instamealmobile.network.ImageViewModel
import com.instamealmobile.R

@Composable
fun SmartAsyncImage(url: String?, modifier: Modifier = Modifier, backupText: String = "", intrinsic: Boolean = false) {

    val viewModel : ImageViewModel = viewModel()
    val painter = rememberAsyncImagePainter(
        model = if (!url.isNullOrEmpty()) url else  "https://placehold.co/600x400.png?text=$backupText",
        placeholder = painterResource(R.drawable.baseline_image_24),
        imageLoader = viewModel.imageLoader
        )
    val fallbackRatio = 14f / 8f
    val aspectRatio = when (painter.state) {
        is AsyncImagePainter.State.Success -> {
            val size = painter.state.painter?.intrinsicSize
            if (size != null && intrinsic && size.width > 0 && size.height > 0) size.width / size.height else if (!intrinsic) 1f else fallbackRatio
        }
        else -> if (intrinsic) fallbackRatio else 1f
    }
    Box(modifier = modifier
        .fillMaxWidth()
        .then(
            aspectRatio.let { Modifier.aspectRatio(it)}
        )
    ) {
        Image(
            painter = painter,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp)),
            contentDescription = null
        )

    }
}