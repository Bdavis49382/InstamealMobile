package com.instamealmobile.ui.pages

import androidx.compose.runtime.Composable
import com.instamealmobile.OpenSheet

@Composable
fun SheetPages(showSheet: OpenSheet, setShowSheet: (OpenSheet) -> Unit) {
    val onDismiss = { setShowSheet(OpenSheet.None)}
    when (showSheet) {
        OpenSheet.AddMeal -> AddRecipePage(onDismiss)
        OpenSheet.ShoppingList -> ShoppingListPage(onDismiss)
        OpenSheet.Share -> SharePage(onDismiss)
        OpenSheet.None -> Unit // Do Nothing
    }
}
