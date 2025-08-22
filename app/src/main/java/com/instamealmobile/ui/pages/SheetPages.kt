package com.instamealmobile.ui.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import com.instamealmobile.OpenAlert
import com.instamealmobile.OpenSheet
import com.instamealmobile.viewModels.NavViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetPages(keepScreenOn: (Boolean) -> Unit,reload: () -> Unit) {
    val nav: NavViewModel = viewModel()
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val closeSheet = {
        coroutineScope.launch {
            lazyListState.scrollToItem(0)
        }
        nav.closeSheet()
    }
    val halfExpanded = SheetDetent(identifier = "halfExpanded") { containerHeight, sheetHeight ->
        containerHeight * 0.5f
    }
    val sheetState = rememberModalBottomSheetState(initialDetent = Hidden,
        detents = listOf(Hidden, halfExpanded, FullyExpanded)
    )
    val context = LocalContext.current

    LaunchedEffect(nav.openSheet) {
        if (nav.openSheet == OpenSheet.None) {
            sheetState.targetDetent = Hidden
            if (nav.openAlert == OpenAlert.None) {
                nav.pickedRecipe = null
            }
        } else if (nav.openSheet == OpenSheet.Household) {
            sheetState.targetDetent = halfExpanded
        } else {
            sheetState.targetDetent = FullyExpanded
        }
    }

    LaunchedEffect(sheetState.currentDetent) {
        if (sheetState.currentDetent == Hidden && nav.openSheet != OpenSheet.None) {
            nav.closeSheet()
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                DragIndication(modifier = Modifier
                    .padding(top = 10.dp)
                    .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(100))
                    .width(32.dp)
                    .height(4.dp)
                )
                when (nav.openSheet) {
                    OpenSheet.PreviewRecipe -> PreviewRecipe(lazyListState)
                    OpenSheet.ShoppingList -> ShoppingListPage(lazyListState)
                    OpenSheet.AddRecipeToMenu -> AddRecipeToMenu(nav.getRecipe()) {
                        Toast.makeText(context, "Recipe Added to Menu", Toast.LENGTH_SHORT).show()
                        closeSheet()
                        reload()
                    }
                    OpenSheet.AddRecipeToFeed -> AddRecipeToFeed(lazyListState)
                    OpenSheet.Household -> HouseholdPage(reload)
                    OpenSheet.ViewRecipe -> ViewRecipe(lazyListState, nav.pickedRecipe, keepScreenOn)
                    OpenSheet.None -> Unit // Do Nothing
                }
            }
        }
    }
}
