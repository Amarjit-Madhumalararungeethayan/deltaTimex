package com.example.timex

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.timex.databinding.ActivityHomeBinding
import java.util.*

public class Fore : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        var cal : Calendar = Calendar.getInstance()

        cal[Calendar.HOUR_OF_DAY] = temp1
            cal[Calendar.MINUTE] = temp2

        cal[Calendar.SECOND] = 0

        Log.d("😑", "${temp1} : ${temp2}")

        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,cal.timeInMillis, pendingIntent)


        return START_STICKY

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
