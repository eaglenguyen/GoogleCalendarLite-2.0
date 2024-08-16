package com.example.calendarcompose

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

const val channelID = "1"
const val TAG = "FIRESTORE"
const val notificationID = 1



class EventNotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.baseline_baby_changing_station_24)
            .setContentTitle("Reminder")
            .setContentText(intent.getStringExtra("eventTitle"))
            .build()

        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }


}