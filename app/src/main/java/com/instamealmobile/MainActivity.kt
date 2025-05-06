package com.instamealmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.instamealmobile.ui.ButtonArea
import com.instamealmobile.ui.pages.HomePage
import com.instamealmobile.ui.pages.ItemConfirmationDialog
import com.instamealmobile.ui.pages.RecipeDialog
import com.instamealmobile.ui.pages.SheetPages
import com.instamealmobile.ui.theme.InstamealMobileTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val (showSheet, setShowSheet) = remember { mutableStateOf(OpenSheet.None)}
            var alertOpen by remember { mutableStateOf(false) }
            var recipeDialogOpen by remember { mutableStateOf(false)}
            var pickedRecipe by remember { mutableStateOf("") }
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }

            if (alertOpen) {
                ItemConfirmationDialog(pickedRecipe,snackbarHostState) {
                    alertOpen = false
                    scope.launch {
                        snackbarHostState.showSnackbar("Added items to shopping list.")
                    }
                }
            }
            else if (recipeDialogOpen) {
                RecipeDialog(pickedRecipe = pickedRecipe) {
                    recipeDialogOpen = false
                    scope.launch {
                        snackbarHostState.showSnackbar("${pickedRecipe} cooked and removed from My Meals.")
                    }
                }
            }

            SheetPages(showSheet, setShowSheet)
            InstamealMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        floatingActionButton = {
                            LargeFloatingActionButton(modifier = Modifier.clip(CircleShape)
                                , onClick = {
                                setShowSheet(OpenSheet.ShoppingList)
                            }) {
                                Icon(painter = painterResource(R.drawable.shoppinglisticon), contentDescription = "Shopping List")
                            }
                        },
                        snackbarHost = {
                            SnackbarHost(snackbarHostState)
                        }
                    ) { innerPadding ->
                        HomePage({meal ->
                            alertOpen = true
                            pickedRecipe = meal
                        }, {meal ->
                            recipeDialogOpen = true
                            pickedRecipe = meal
                        },Modifier.padding(innerPadding))

                    }
                }
            }
        }
    }
}


fun makeLongList(num : Int = 2) : MutableList<String> {
    val meals = mutableListOf<String>()
    meals.add("Hamburger");
    meals.add("Double Cheeseburger")
    for (i in 1..num) {
        meals.add("bla")
    }
    return meals
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    val (showSheet, setShowSheet) = remember { mutableStateOf(OpenSheet.None)}

    SheetPages(showSheet, setShowSheet)
    InstamealMobileTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorResource(R.color.board)
        ) {
            HomePage({},{})
            ButtonArea(setShowSheet)

        }
    }
}