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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.instamealmobile.ui.pages.HomePage
import com.instamealmobile.ui.pages.InviteToHousehold
import com.instamealmobile.ui.pages.JoinHousehold
import com.instamealmobile.ui.pages.SheetPages
import com.instamealmobile.ui.theme.InstamealMobileTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val (showSheet, setShowSheet) = remember { mutableStateOf(OpenSheet.None)}
            var (openAlert, setAlert) = remember { mutableStateOf(OpenAlert.None) }
            var recipeDialogOpen by remember { mutableStateOf(false)}
            var pickedRecipe by remember { mutableStateOf("") }
//            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }

            if (openAlert == OpenAlert.Join) {
                JoinHousehold { setAlert(OpenAlert.None) }
//                ItemConfirmationDialog(pickedRecipe,snackbarHostState) {
//                    alertOpen = false
//                    scope.launch {
//                        snackbarHostState.showSnackbar("Added items to shopping list.")
//                    }
//                }
            } else if (openAlert == OpenAlert.Invite) {
                InviteToHousehold { setAlert(OpenAlert.None) }
            }

            SheetPages(showSheet, setShowSheet, setAlert, pickedRecipe)
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
                            setShowSheet(OpenSheet.PreviewRecipe)
                            pickedRecipe = meal
                        }, {meal ->
                            setShowSheet(OpenSheet.ViewRecipe)
                            pickedRecipe = meal
                        }, {
                            setShowSheet(OpenSheet.AddRecipeToFeed)
                        }, {
                            setShowSheet(OpenSheet.Household)
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