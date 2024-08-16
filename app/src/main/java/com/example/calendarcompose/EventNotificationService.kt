package com.example.calendarcompose

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

const val notificationIDService = 1

class EventNotificationService(
    private val contexts: Context
) {

    fun showNotification(context: Context, time: Long, title: String) {
        val activityIntent = Intent(context, EventNotificationReceiver::class.java).apply {
            putExtra("eventTitle", title)

        }
        val eventIntent = PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            eventIntent
        )
    }



}