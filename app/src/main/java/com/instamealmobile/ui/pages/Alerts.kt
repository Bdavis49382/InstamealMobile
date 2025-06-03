package com.instamealmobile.ui.pages

import androidx.compose.runtime.Composable
import com.instamealmobile.OpenAlert
import com.instamealmobile.data.Recipe

@Composable
fun Alerts(openAlert: OpenAlert, setAlert: (OpenAlert) -> Unit, pickedRecipe: Recipe) {
    when (openAlert) {
        OpenAlert.Join -> {
            JoinHousehold { setAlert(OpenAlert.None) }
        }
        OpenAlert.Invite -> {
            InviteToHousehold { setAlert(OpenAlert.None) }
        }
        OpenAlert.Rating -> {
            if (pickedRecipe.id.isNullOrEmpty()) {
                throw Exception("Tried to finish a recipe without one selected!")
            }
            RatingAlert(pickedRecipe.id) {setAlert(OpenAlert.None)}
        }
        OpenAlert.None -> {}
    }
}