package com.instamealmobile.ui.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.composables.core.ModalBottomSheet
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import com.instamealmobile.OpenAlert
import com.instamealmobile.OpenSheet
import com.instamealmobile.data.Recipe
import com.instamealmobile.data.RecipeIdentifier
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
        setShowSheet(OpenSheet.None)
    }
    val halfExpanded = SheetDetent(identifier = "halfExpanded") { containerHeight, sheetHeight ->
        containerHeight * 0.5f
    }
    val sheetState = rememberModalBottomSheetState(initialDetent = Hidden,
        detents = listOf(Hidden, halfExpanded, FullyExpanded)
    )
    val context = LocalContext.current

    LaunchedEffect(showSheet) {
        if (showSheet == OpenSheet.None) {
            sheetState.targetDetent = Hidden
        } else if (showSheet == OpenSheet.Household) {
            sheetState.targetDetent = halfExpanded
        } else {
            sheetState.targetDetent = FullyExpanded
        }
    }

    LaunchedEffect(sheetState.currentDetent) {
        if (sheetState.currentDetent == Hidden && showSheet != OpenSheet.None) {
            setShowSheet(OpenSheet.None)
        }
    }

    ModalBottomSheet(
        state = sheetState
    ) {
        Scrim()
        Sheet(
            contentPadding = PaddingValues(top = 15.dp),
            modifier = Modifier.fillMaxHeight().padding(top=40.dp)
                .shadow(4.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(MaterialTheme.colorScheme.background)
                .widthIn(max = 640.dp)
                .fillMaxWidth()
                .imePadding()
        ) {
            when (showSheet) {
                OpenSheet.PreviewRecipe -> PreviewRecipe(lazyListState, { recipe ->
                    setPickedRecipe(recipe)
                    setAddRecipePurpose(Purpose.FeedEdit)
                    setShowSheet(OpenSheet.AddRecipeToFeed)
                }, RecipeIdentifier.factory(pickedRecipe)) {
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
}
