package com.instamealmobile.ui.pages

import androidx.compose.runtime.Composable
import com.instamealmobile.OpenAlert
import com.instamealmobile.OpenSheet
import com.instamealmobile.data.Recipe

@Composable
fun SheetPages(showSheet: OpenSheet, setShowSheet: (OpenSheet) -> Unit, setAlert: (OpenAlert) -> Unit, pickedRecipe: Recipe = Recipe(title = "", img_link = ""), setPickedRecipe: (Recipe) -> Unit) {
    val onDismiss = { setShowSheet(OpenSheet.None)}
    when (showSheet) {
        OpenSheet.PreviewRecipe -> PreviewRecipe(onDismiss, {
            setShowSheet(OpenSheet.AddRecipeToMenu)
            setPickedRecipe(it) }, pickedRecipe)
        OpenSheet.ShoppingList -> ShoppingListPage(onDismiss)
        OpenSheet.AddRecipeToMenu -> AddRecipeToMenu(onDismiss, {onDismiss()}, pickedRecipe)
        OpenSheet.AddRecipeToFeed -> AddRecipeToFeed(onDismiss, {
            setPickedRecipe(it)
            setShowSheet(OpenSheet.PreviewRecipe)
        })
        OpenSheet.Household -> HouseholdPage(onDismiss, {setAlert(OpenAlert.Join)}, {setAlert(OpenAlert.Invite)})
        OpenSheet.ViewRecipe -> ViewRecipe(onDismiss, {onDismiss()}, pickedRecipe)
        OpenSheet.None -> Unit // Do Nothing
    }
}
