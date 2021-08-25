package com.example.timex

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.example.timex.databinding.ActivityHomeBinding
import java.util.*

lateinit var msg : String

class Home : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmManager2: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var pendingIntent2: PendingIntent
    private lateinit var calendar : Calendar
    private lateinit var calendar2 : Calendar


    override fun onBackPressed() {
        finishAffinity()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reset.isVisible = false

        createNotif()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#808080")
        }

        binding.start.setOnClickListener() {

            msg = binding.event.text.toString()
            binding.reset.isVisible = true

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = binding.time.hour
            calendar[Calendar.MINUTE] = binding.time.minute
            calendar[Calendar.SECOND] = 0

            calendar2 = Calendar.getInstance()
            if(binding.time.minute < 15){
                calendar2[Calendar.HOUR_OF_DAY] = binding.time.hour - 1
                calendar2[Calendar.MINUTE] = 60 + (binding.time.minute - 15)
                calendar2[Calendar.SECOND] = 0
            }
            calendar2[Calendar.HOUR_OF_DAY] = binding.time.hour
            calendar2[Calendar.MINUTE] = (binding.time.minute - 15)
            calendar2[Calendar.SECOND] = 0


            alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager2 = getSystemService(ALARM_SERVICE) as AlarmManager

            val intent = Intent(this, AlarmReceiver::class.java)

            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
            pendingIntent2 = PendingIntent.getBroadcast(this, 0, intent, 0)

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.timeInMillis, pendingIntent)
            alarmManager2.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar2.timeInMillis, pendingIntent)

            Toast.makeText(this, "Reminder Set", Toast.LENGTH_SHORT).show()

        }

        binding.reset.setOnClickListener(){
            cancelTimer()
        }

    }

    private fun cancelTimer() {

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.cancel(pendingIntent)

        Toast.makeText(this, "Reminder Cancelled", Toast.LENGTH_SHORT).show()
    }

    fun createNotif(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("amar", "event", NotificationManager.IMPORTANCE_HIGH)
            channel.description = binding.event.text.toString()
            val notifM = getSystemService(NotificationManager::class.java)

            notifM.createNotificationChannel(channel)
        }
    }
}