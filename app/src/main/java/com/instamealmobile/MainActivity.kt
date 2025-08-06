package com.instamealmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.LaunchedEffect
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
import androidx.paging.compose.collectAsLazyPagingItems
import com.instamealmobile.ui.Header
import com.instamealmobile.ui.pages.Alerts
import com.instamealmobile.ui.pages.HomePage
import com.instamealmobile.ui.pages.SheetPages
import com.instamealmobile.ui.theme.InstamealMobileTheme
import com.instamealmobile.viewModels.AuthViewModel
import com.instamealmobile.viewModels.FeedViewModel
import com.instamealmobile.viewModels.MenuViewModel
import com.instamealmobile.viewModels.SearchBarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authViewModel: AuthViewModel = viewModel()
            val feedViewModel: FeedViewModel = viewModel()
            val items = feedViewModel.pagingFlow.collectAsLazyPagingItems()
            val menuViewModel: MenuViewModel = viewModel()
            val searchBarViewModel: SearchBarViewModel = viewModel()
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val credentialManager = CredentialManager.create(context)

            LaunchedEffect(Unit) {
//                authViewModel.logout(credentialManager)
                if (!authViewModel.checkLogin()) {
                    authViewModel.login(coroutineScope,credentialManager, context) {
                        menuViewModel.getMenu()
                        searchBarViewModel.getTags()
                        items.retry()
                    }
                }
            }
             val darkColors = darkColorScheme(
                 primaryContainer = Color(0xFFA5B97D),
                onPrimaryContainer = Color.Black,
                secondaryContainer = Color.White,
                onSecondaryContainer = Color.Black,
                 primary = Color(0xFF6E412F),
                 onPrimary = Color.White,
                 secondary  = Color(0xFFe6ebe4),
                 onSecondary = Color.Black
            )

            val lightColors = lightColorScheme(
                primaryContainer = Color(0xFFA5B97D),
                onPrimaryContainer = Color.Black,
                secondaryContainer = Color.White,
                onSecondaryContainer = Color.Black,
                primary = Color(0xFF6E412F),
                onPrimary = Color.White,
                secondary  = Color(0xFFe6ebe4),
                onSecondary = Color.Black
            )

            val typography = Typography(
                headlineMedium = TextStyle(
                    fontSize = 35.sp,
                    fontFamily = FontFamily(Font(R.font.caveat_brush, FontWeight.Bold))
                )
            )

            InstamealMobileTheme {
                MaterialTheme(colorScheme = if (isSystemInDarkTheme()) darkColors else lightColors, typography=typography) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Alerts {
                            menuViewModel.getMenu()
                            items.refresh()
                        }

                        SheetPages {
                            menuViewModel.getMenu()
                            items.refresh()
                        }
                        Scaffold(topBar = {Header()}) { innerPadding ->
                            HomePage(Modifier.padding(innerPadding))
                        }
                    }
                }
            }
        }
    }
}