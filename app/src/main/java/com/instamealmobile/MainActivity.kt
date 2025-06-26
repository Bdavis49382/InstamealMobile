package com.instamealmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.instamealmobile.data.Recipe
import com.instamealmobile.ui.Header
import com.instamealmobile.ui.pages.Alerts
import com.instamealmobile.ui.pages.HomePage
import com.instamealmobile.ui.pages.SheetPages
import com.instamealmobile.ui.theme.InstamealMobileTheme
import com.instamealmobile.viewModels.AuthViewModel
import com.instamealmobile.viewModels.FeedViewModel
import com.instamealmobile.viewModels.MenuViewModel
import com.instamealmobile.viewModels.Purpose
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val (showSheet, setShowSheet) = remember { mutableStateOf(OpenSheet.None)}
            var (openAlert, setAlert) = remember { mutableStateOf(OpenAlert.None) }
            val (pickedRecipe, setPickedRecipe) = remember { mutableStateOf(Recipe(title="", img_link = "")) }
            val (addToFeedPurpose, setAddToFeedPurpose) = remember {mutableStateOf(Purpose.AddNew)}
            val authViewModel: AuthViewModel = viewModel()
            val menuViewModel: MenuViewModel = viewModel()
            val feedViewModel: FeedViewModel = viewModel()
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val credentialManager = CredentialManager.create(context)

            LaunchedEffect(Unit) {
//                authViewModel.logout(credentialManager)
                if (!authViewModel.checkLogin()) {
                    authViewModel.login(coroutineScope,credentialManager, context) {
                        menuViewModel.getMenu()
                        feedViewModel.fetchFeed()
                    }
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

            val typography = Typography(
                headlineMedium = TextStyle(
                    fontSize = 35.sp,
                    fontFamily = FontFamily(Font(R.font.caveat_brush, FontWeight.Bold))
                )
            )

            InstamealMobileTheme {
                MaterialTheme(colorScheme = darkColors, typography=typography) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Alerts(openAlert, setAlert, pickedRecipe) {
                            feedViewModel.fetchFeed()
                            menuViewModel.getMenu()
                        }

                        SheetPages(showSheet, setShowSheet, setAlert, pickedRecipe, setPickedRecipe, addToFeedPurpose, setAddToFeedPurpose)
                        Scaffold(topBar = {Header { setShowSheet(OpenSheet.Household) } }) { innerPadding ->
                            HomePage({meal ->
                                setShowSheet(OpenSheet.PreviewRecipe)
                                setPickedRecipe(meal)
                            }, {meal ->
                                setShowSheet(OpenSheet.ViewRecipe)
                                setPickedRecipe(meal)
                            }, {
                                setAddToFeedPurpose(Purpose.AddNew)
                                setShowSheet(OpenSheet.AddRecipeToFeed)
                                setPickedRecipe(Recipe(title=""))
                            }, {
                                setShowSheet(OpenSheet.ShoppingList)
                            },
                            Modifier.padding(innerPadding))
                        }
                    }
                }
            }
        }
    }
}