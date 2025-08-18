package com.instamealmobile.ui.pages

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.OpenAlert
import com.instamealmobile.data.RecipeIdentifier
import com.instamealmobile.viewModels.NavViewModel

@Composable
fun Alerts(reload: () -> Unit) {
    val nav: NavViewModel = viewModel()
    when (nav.openAlert) {
        OpenAlert.Join -> {
            JoinHousehold(reload)
        }
        OpenAlert.Invite -> {
            InviteToHousehold()
        }
        OpenAlert.Rating -> {
            if (nav.pickedRecipe == null) {
                throw Exception("Tried to finish a recipe without one selected!")
            }
            if (nav.pickedRecipe is RecipeIdentifier.RecipeId) {
                RatingAlert((nav.pickedRecipe as RecipeIdentifier.RecipeId).id)
            }
        }
        OpenAlert.Link -> {
            GetRecipeByLink()
        }
        OpenAlert.None -> {}
    }
}