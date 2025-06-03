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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.data.Recipe
import com.instamealmobile.ui.pages.Alerts
import com.instamealmobile.ui.pages.HomePage
import com.instamealmobile.ui.pages.SheetPages
import com.instamealmobile.ui.theme.InstamealMobileTheme
import com.instamealmobile.viewModels.HouseholdViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val (showSheet, setShowSheet) = remember { mutableStateOf(OpenSheet.None)}
            var (openAlert, setAlert) = remember { mutableStateOf(OpenAlert.None) }
            val (pickedRecipe, setPickedRecipe) = remember { mutableStateOf(Recipe(title="", img_link = "")) }
            val snackbarHostState = remember { SnackbarHostState() }
            val viewModel: HouseholdViewModel =  viewModel()
            val householdIdState by viewModel.householdId.observeAsState()

            LaunchedEffect(Unit) {
                viewModel.getId()
            }

            InstamealMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Alerts(openAlert, setAlert, pickedRecipe)

                    SheetPages(showSheet, setShowSheet, setAlert, pickedRecipe, setPickedRecipe)
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
                            setPickedRecipe(meal)
                        }, {meal ->
                            setShowSheet(OpenSheet.ViewRecipe)
                            setPickedRecipe(meal)
                        }, {
                            setShowSheet(OpenSheet.AddRecipeToFeed)
                            setPickedRecipe(Recipe(title=""))
                        }, {
                            setShowSheet(OpenSheet.Household)
                        },Modifier.padding(innerPadding))

                    }
                }
            }
        }
    }
}