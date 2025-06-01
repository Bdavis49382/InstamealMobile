package com.instamealmobile.ui.pages

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.instamealmobile.OpenAlert
import com.instamealmobile.OpenSheet
import com.instamealmobile.data.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetPages(showSheet: OpenSheet, setShowSheet: (OpenSheet) -> Unit, setAlert: (OpenAlert) -> Unit, pickedRecipe: Recipe = Recipe(title = "", img_link = ""), setPickedRecipe: (Recipe) -> Unit) {
    val closeSheet = { setShowSheet(OpenSheet.None)}
    val fullyExpand = showSheet != OpenSheet.Household
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = fullyExpand)
    val context = LocalContext.current

    if (showSheet == OpenSheet.None) {
        return
    }
    ModalBottomSheet(onDismissRequest = { closeSheet() },
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(),
        dragHandle = { BottomSheetDefaults.DragHandle()}
    ) {
        when (showSheet) {
            OpenSheet.PreviewRecipe -> PreviewRecipe(closeSheet, pickedRecipe) {
                setShowSheet(OpenSheet.AddRecipeToMenu)
                setPickedRecipe(it)
            }
            OpenSheet.ShoppingList -> ShoppingListPage()
            OpenSheet.AddRecipeToMenu -> AddRecipeToMenu(pickedRecipe) {
                Toast.makeText(context, "Recipe Added to Menu", Toast.LENGTH_SHORT).show()
                closeSheet()
            }
            OpenSheet.AddRecipeToFeed -> AddRecipeToFeed {
                setPickedRecipe(it)
                setShowSheet(OpenSheet.PreviewRecipe)
                Toast.makeText(context, "Recipe Added to My Recipes", Toast.LENGTH_SHORT).show()
            }
            OpenSheet.Household -> HouseholdPage(
                { setAlert(OpenAlert.Join) },
                { setAlert(OpenAlert.Invite) })
            OpenSheet.ViewRecipe -> ViewRecipe(pickedRecipe) {
                Toast.makeText(context, "Recipe Finished", Toast.LENGTH_SHORT).show()
                closeSheet()
            }
            OpenSheet.None -> Unit // Do Nothing
        }
    }
}
