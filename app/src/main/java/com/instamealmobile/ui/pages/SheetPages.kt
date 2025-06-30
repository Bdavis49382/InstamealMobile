package com.instamealmobile.ui.pages

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.instamealmobile.OpenAlert
import com.instamealmobile.OpenSheet
import com.instamealmobile.data.Recipe
import com.instamealmobile.viewModels.Purpose
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetPages(showSheet: OpenSheet, setShowSheet: (OpenSheet) -> Unit, setAlert: (OpenAlert) -> Unit, pickedRecipe: Recipe = Recipe(title = "", img_link = ""), setPickedRecipe: (Recipe) -> Unit, addToFeedPurpose: Purpose, setAddRecipePurpose: (Purpose) -> Unit, reload: () -> Unit) {
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
    val fullPages = listOf<OpenSheet>(OpenSheet.ShoppingList, OpenSheet.AddRecipeToFeed, OpenSheet.AddRecipeToMenu,
        OpenSheet.PreviewRecipe, OpenSheet.ViewRecipe)
    val halfPages = listOf<OpenSheet>(OpenSheet.Household)
    var oldValue by remember { mutableStateOf(SheetValue.Hidden)}
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = fullPages.contains(showSheet),
        confirmValueChange = {
            newState ->
            when (newState) {
                SheetValue.Hidden -> (fullPages.contains(showSheet) && isAtTop)
                        || oldValue == SheetValue.PartiallyExpanded
                SheetValue.Expanded -> true
                SheetValue.PartiallyExpanded -> isAtTop
            }
        })
    val context = LocalContext.current

    if (showSheet == OpenSheet.None) {
        return
    }

    LaunchedEffect(sheetState.currentValue) {
        if (oldValue == SheetValue.Hidden
            && sheetState.currentValue == SheetValue.PartiallyExpanded
            && !halfPages.contains(showSheet)
            ) {
            sheetState.expand()
        }
        oldValue = sheetState.currentValue
    }
    ModalBottomSheet(onDismissRequest = { closeSheet() },
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight().padding(top=40.dp),
        dragHandle = { BottomSheetDefaults.DragHandle()}
    ) {
        when (showSheet) {
            OpenSheet.PreviewRecipe -> PreviewRecipe(lazyListState, { recipe ->
                setPickedRecipe(recipe)
                setAddRecipePurpose(Purpose.FeedEdit)
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
            OpenSheet.AddRecipeToFeed -> AddRecipeToFeed(pickedRecipe, lazyListState, addToFeedPurpose) {
                setPickedRecipe(it)
                if (addToFeedPurpose == Purpose.AddNew || addToFeedPurpose == Purpose.FeedEdit) {
                    setShowSheet(OpenSheet.PreviewRecipe)
                    if (addToFeedPurpose == Purpose.AddNew) {
                        Toast.makeText(context, "Recipe Added to My Recipes", Toast.LENGTH_SHORT).show()
                    }
                } else if (addToFeedPurpose == Purpose.MenuEdit) {
                    setShowSheet(OpenSheet.ViewRecipe)
                    Toast.makeText(context, "Recipe Updated", Toast.LENGTH_SHORT).show()
                }
            }
            OpenSheet.Household -> HouseholdPage(
                { setAlert(OpenAlert.Join) },
                { setAlert(OpenAlert.Invite) }, reload)
            OpenSheet.ViewRecipe -> ViewRecipe(lazyListState, pickedRecipe, { recipe ->
                    setPickedRecipe(recipe)
                    setAddRecipePurpose(Purpose.MenuEdit)
                    setShowSheet(OpenSheet.AddRecipeToFeed)
            }) {
                closeSheet()
                setAlert(OpenAlert.Rating)
            }
            OpenSheet.None -> Unit // Do Nothing
        }
    }
}
