package com.instamealmobile.ui.pages

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.instamealmobile.OpenAlert
import com.instamealmobile.OpenSheet
import com.instamealmobile.data.Recipe

@Composable
fun SheetPages(showSheet: OpenSheet, setShowSheet: (OpenSheet) -> Unit, setAlert: (OpenAlert) -> Unit, pickedRecipe: Recipe = Recipe(title = "", img_link = ""), setPickedRecipe: (Recipe) -> Unit) {
    val onDismiss = { setShowSheet(OpenSheet.None)}
    val context = LocalContext.current
    when (showSheet) {
        OpenSheet.PreviewRecipe -> PreviewRecipe(onDismiss, {
            setShowSheet(OpenSheet.AddRecipeToMenu)
            setPickedRecipe(it) }, pickedRecipe)
        OpenSheet.ShoppingList -> ShoppingListPage(onDismiss)
        OpenSheet.AddRecipeToMenu -> AddRecipeToMenu(onDismiss, {
            Toast.makeText(context, "Recipe Added to Menu", Toast.LENGTH_SHORT).show()
            onDismiss()}, pickedRecipe)
        OpenSheet.AddRecipeToFeed -> AddRecipeToFeed(onDismiss, {
            setPickedRecipe(it)
            setShowSheet(OpenSheet.PreviewRecipe)
            Toast.makeText(context, "Recipe Added to My Recipes", Toast.LENGTH_SHORT).show()
        })
        OpenSheet.Household -> HouseholdPage(onDismiss, {setAlert(OpenAlert.Join)}, {setAlert(OpenAlert.Invite)})
        OpenSheet.ViewRecipe -> ViewRecipe(onDismiss, {
            Toast.makeText(context, "Recipe Finished", Toast.LENGTH_SHORT).show()
            onDismiss()}, pickedRecipe)
        OpenSheet.None -> Unit // Do Nothing
    }
}
