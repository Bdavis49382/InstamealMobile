package com.instamealmobile.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.instamealmobile.OpenAlert
import com.instamealmobile.OpenSheet
import com.instamealmobile.data.Recipe
import com.instamealmobile.data.RecipeIdentifier
import com.instamealmobile.data.RecipeIdentifier.FullRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(): ViewModel() {
    var openSheet by mutableStateOf(OpenSheet.None)
    var openAlert by mutableStateOf(OpenAlert.None)
    var pickedRecipe: RecipeIdentifier? by mutableStateOf(null)
    var addToFeedPurpose: Purpose by mutableStateOf(Purpose.AddNew)

    fun navigateTo(sheet: OpenSheet, recipe: RecipeIdentifier? = null, purpose: Purpose = Purpose.AddNew) {
        openSheet = sheet
        pickedRecipe = recipe
        addToFeedPurpose = purpose
    }
    fun navigateTo(alert: OpenAlert) {
        openAlert = alert
    }
    fun closeAlert() {
        openAlert = OpenAlert.None
    }

    fun closeSheet() {
        openSheet = OpenSheet.None
    }

    fun getRecipe(): Recipe {
        if (pickedRecipe is FullRecipe) {
            return (pickedRecipe as FullRecipe).recipe
        } else {
            return Recipe(title="")
        }
    }
}
