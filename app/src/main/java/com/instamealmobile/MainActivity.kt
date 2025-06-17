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
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.credentials.CredentialManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.data.Recipe
import com.instamealmobile.ui.pages.Alerts
import com.instamealmobile.ui.pages.HomePage
import com.instamealmobile.ui.pages.SheetPages
import com.instamealmobile.ui.theme.InstamealMobileTheme
import com.instamealmobile.viewModels.AuthViewModel
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
            val authViewModel: AuthViewModel = viewModel()
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val credentialManager = CredentialManager.create(context)

            LaunchedEffect(Unit) {
                if (!authViewModel.checkLogin()) {
                    authViewModel.login(coroutineScope,credentialManager, context)
                }
            }
             val darkColors = darkColorScheme(
//                primaryContainer = Color(0xFFC6F5D0),
                 primaryContainer = Color(0xFFA5B97D),
                onPrimaryContainer = Color.Black,
                secondaryContainer = Color.White,
                onSecondaryContainer = Color.Black,
                 background = Color.Black,
                 onBackground = Color.White,
                 primary = Color(0xFF6E412F),
                 onPrimary = Color.White
            )

            InstamealMobileTheme {
                MaterialTheme(colorScheme = darkColors) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Alerts(openAlert, setAlert, pickedRecipe)

                        SheetPages(showSheet, setShowSheet, setAlert, pickedRecipe, setPickedRecipe)
                        Scaffold(
                            floatingActionButton = {
                                LargeFloatingActionButton(modifier = Modifier.clip(CircleShape),
                                    containerColor = MaterialTheme.colorScheme.primary
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
}