package com.instamealmobile.ui.pages

import androidx.compose.runtime.Composable
import com.instamealmobile.OpenSheet

@Composable
fun SheetPages(showSheet: OpenSheet, setShowSheet: (OpenSheet) -> Unit, pickedRecipe: String = "") {
    val onDismiss = { setShowSheet(OpenSheet.None)}
    when (showSheet) {
        OpenSheet.PreviewRecipe -> PreviewRecipe(onDismiss, {setShowSheet(OpenSheet.AddRecipeToMenu)}, pickedRecipe)
        OpenSheet.ShoppingList -> ShoppingListPage(onDismiss)
        OpenSheet.AddRecipeToMenu -> AddRecipeToMenu(onDismiss, {onDismiss()}, pickedRecipe)
        OpenSheet.AddRecipeToFeed -> AddRecipeToFeed(onDismiss, {setShowSheet(OpenSheet.PreviewRecipe)})
        OpenSheet.Household -> SharePage(onDismiss)
        OpenSheet.ViewRecipe -> SharePage(onDismiss)
        OpenSheet.None -> Unit // Do Nothing
    }
}
