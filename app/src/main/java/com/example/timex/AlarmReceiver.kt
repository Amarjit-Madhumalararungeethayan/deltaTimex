package com.example.timex

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_RECEIVER_FOREGROUND
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        var notifId = intent?.getIntExtra("notifID", 0)

        var i = Intent(context, Home::class.java)
        //alarmManager.cancel(pendingIntent)
        var pendingIntent = PendingIntent.getActivity(context, 0 , i, 0)
        var builder = NotificationCompat.Builder(context!!,"amar")
        builder.setSmallIcon(R.drawable.timex)
            .setContentTitle("Event !!!")
            .setContentText("You have a reminder")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setPriority(Notification.PRIORITY_MAX)

        val notif = NotificationManagerCompat.from(context)
        notif.notify(123,builder.build())
    }


}