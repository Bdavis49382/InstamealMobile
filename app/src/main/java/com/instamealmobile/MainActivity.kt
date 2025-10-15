package com.instamealmobile

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.instamealmobile.data.RecipeIdentifier
import com.instamealmobile.notifications.MealNotificationService
import com.instamealmobile.ui.Header
import com.instamealmobile.ui.ImagePurpose
import com.instamealmobile.ui.pages.Alerts
import com.instamealmobile.ui.pages.HomePage
import com.instamealmobile.ui.pages.SheetPages
import com.instamealmobile.ui.theme.InstamealMobileTheme
import com.instamealmobile.viewModels.AddRecipeToFeedViewModel
import com.instamealmobile.viewModels.AuthViewModel
import com.instamealmobile.viewModels.FeedViewModel
import com.instamealmobile.viewModels.MenuViewModel
import com.instamealmobile.viewModels.NavViewModel
import com.instamealmobile.viewModels.SearchBarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionState = ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
        if (permissionState != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
        createNotificationChannel()
        setContent {
            val authViewModel: AuthViewModel = viewModel()
            val feedViewModel: FeedViewModel = viewModel()
            val items = feedViewModel.pagingFlow.collectAsLazyPagingItems()
            val menuViewModel: MenuViewModel = viewModel()
            val navViewModel : NavViewModel = viewModel()
            val addtoFeedViewModel: AddRecipeToFeedViewModel = viewModel()
            val searchBarViewModel: SearchBarViewModel = viewModel()
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val credentialManager = CredentialManager.create(context)

            LaunchedEffect(Unit) {
                val intent = (context as? Activity)?.intent
                when (intent?.action) {
                    Intent.ACTION_SEND -> {
                        if (intent.type == "text/plain") {
                            val url = intent.getStringExtra(Intent.EXTRA_TEXT)
                            if (!url.isNullOrEmpty()) {
                                addtoFeedViewModel.src_link.value = url
                                navViewModel.navigateTo(OpenSheet.AddRecipeToFeed)
                                Toast.makeText(context, "Entering information...", Toast.LENGTH_LONG).show()
                                addtoFeedViewModel.parseWebsite({
                                    Toast.makeText(context, "Recipe imported. Review and save.", Toast.LENGTH_SHORT).show()
                                },{
                                    Toast.makeText(context, "The provided link was invalid.", Toast.LENGTH_SHORT).show()

                                }) {
                                    Toast.makeText(context, "Website did not support importing. Take a screenshot and import as an image.", Toast.LENGTH_SHORT).show()
                                }

                            }
                        } else if (intent.type?.startsWith("image/") == true) {
                            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)?.let { url ->
                                navViewModel.navigateTo(OpenSheet.AddRecipeToFeed)
                                Toast.makeText(context, "Entering information...", Toast.LENGTH_SHORT).show()
                                addtoFeedViewModel.parseText(url,context,ImagePurpose.TextParsing){
                                    Toast.makeText(context, "Recipe imported. Review and save.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    else -> {
                        val id = intent?.getStringExtra("id")
                        if (id != null) {
                            navViewModel.navigateTo(OpenSheet.ViewRecipe, RecipeIdentifier.MenuIndex(id.toInt()))
                        }
                    }
                }

            }

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

                        SheetPages(::keepScreenOn) {
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
    fun keepScreenOn(value: Boolean) {
        if (value) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
    fun createNotificationChannel() {
        val channel = NotificationChannel(
            MealNotificationService.MEAL_CHANNEL_ID,
            "Meal Reminders",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "Used for meal reminders"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}