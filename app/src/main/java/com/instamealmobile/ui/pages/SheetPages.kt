package com.instamealmobile.ui.pages

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.instamealmobile.OpenAlert
import com.instamealmobile.OpenSheet
import com.instamealmobile.data.Recipe
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetPages(showSheet: OpenSheet, setShowSheet: (OpenSheet) -> Unit, setAlert: (OpenAlert) -> Unit, pickedRecipe: Recipe = Recipe(title = "", img_link = ""), setPickedRecipe: (Recipe) -> Unit) {
    val fullyExpand = showSheet != OpenSheet.Household
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val closeSheet = {
        coroutineScope.launch {
            lazyListState.scrollToItem(0)
        }
        setShowSheet(OpenSheet.None)}
    val isAtTop by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex == 0 }
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = fullyExpand,
        confirmValueChange = {
            newState ->
            isAtTop || newState != SheetValue.Hidden})
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
            OpenSheet.PreviewRecipe -> PreviewRecipe(lazyListState, { recipe ->
                setPickedRecipe(recipe)
                setShowSheet(OpenSheet.AddRecipeToFeed)
            }, pickedRecipe) {
                setShowSheet(OpenSheet.AddRecipeToMenu)
                setPickedRecipe(it)
            }
            OpenSheet.ShoppingList -> ShoppingListPage(lazyListState)
            OpenSheet.AddRecipeToMenu -> AddRecipeToMenu(pickedRecipe) {
                Toast.makeText(context, "Recipe Added to Menu", Toast.LENGTH_SHORT).show()
                closeSheet()
            }
            OpenSheet.AddRecipeToFeed -> AddRecipeToFeed(pickedRecipe) {
                setPickedRecipe(it)
                setShowSheet(OpenSheet.PreviewRecipe)
            }
            OpenSheet.Household -> HouseholdPage(
                { setAlert(OpenAlert.Join) },
                { setAlert(OpenAlert.Invite) })
            OpenSheet.ViewRecipe -> ViewRecipe(lazyListState, pickedRecipe) {
                closeSheet()
                setAlert(OpenAlert.Rating)
            }
            OpenSheet.None -> Unit // Do Nothing
        }
    }
}
