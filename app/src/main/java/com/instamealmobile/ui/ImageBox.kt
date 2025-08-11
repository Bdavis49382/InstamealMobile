package com.instamealmobile.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.R
import com.instamealmobile.data.ApiState
import com.instamealmobile.viewModels.AddRecipeToFeedViewModel

@Composable
fun ImageBox(openPopup: () -> Unit) {
    val viewModel: AddRecipeToFeedViewModel =  viewModel()
    val imgLinkState by viewModel.img_link.collectAsState()

    Box(modifier = Modifier.fillMaxWidth().focusProperties {canFocus = false}
        , contentAlignment = Alignment.Center) {
        Crossfade(targetState = imgLinkState, label = "ContentSwitch") { screenState ->
            when (screenState) {
                is ApiState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize().border(
                            width=1.dp,
                            shape=RoundedCornerShape(10.dp),
                            color=MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        CircularProgressIndicator(modifier = Modifier.padding(vertical = 80.dp))
                    }
                }

                is ApiState.Success -> if (imgLinkState is ApiState.Success){
                    val img_link = (imgLinkState as ApiState.Success<String>).data
                    SmartAsyncImage(
                        url = img_link,
                        backupText = viewModel.title.value,
                        modifier = Modifier
                            .clickable {
                                openPopup()
                            }
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
                        modifier = Modifier.fillMaxSize().border(
                            width=1.dp,
                            shape=RoundedCornerShape(10.dp),
                            color=MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        OutlinedButton({
                            openPopup()
                        }, modifier = Modifier.padding(vertical = 80.dp)) {
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