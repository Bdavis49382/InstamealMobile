package com.instamealmobile.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.instamealmobile.MainActivity
import com.instamealmobile.R

class MealNotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(mealName: String, id: String) {
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("id",id)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, MEAL_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_calendar_today_24)
            .setContentTitle("Ready to make $mealName?")
            .setContentText("You scheduled to make $mealName today, tap to get started!")
            .setContentIntent(activityPendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            1, notification
        )
    }

    companion object {
        const val MEAL_CHANNEL_ID = "meal_channel"
    }
}