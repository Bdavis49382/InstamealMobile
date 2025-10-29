package com.instamealmobile.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.instamealmobile.network.MenuService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {
    @Inject
    lateinit var menuService: MenuService
    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.getStringExtra("title") ?: return
        val message = intent.getStringExtra("message") ?: return
        val notificationService = MealNotificationService(context)
        runBlocking {
            val menuItem = menuService.getRecipeById(message)
            if (LocalDate.from(
                    menuItem.date?.toInstant()?.atZone(ZoneId.systemDefault())
                ) == LocalDateTime.now().toLocalDate()
            ) {
                notificationService.showNotification(title, message)
            } else {
                Log.i(
                    "ALARM",
                    "did not push notification as the date no longer matches the menu item."
                )
            }
        }
    }
}