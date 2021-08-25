package com.example.timex

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.example.timex.databinding.ActivityHomeBinding
import java.util.*

lateinit var msg : String

lateinit var alarmManager: AlarmManager
lateinit var pendingIntent: PendingIntent

class Home : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

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
        createRem()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#808080")
        }


        binding.start.setOnClickListener() {

            msg = binding.event.text.toString()
            binding.reset.isVisible = true

            val kalendar = Calendar.getInstance()
            val hour12hrs = kalendar.get(Calendar.HOUR_OF_DAY)
            val minutes = kalendar.get(Calendar.MINUTE)

            Log.d("💔", "${hour12hrs}:${minutes}")

            var t1 = hour12hrs*60*60 + minutes*60

            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = binding.time.hour
            calendar[Calendar.MINUTE] = binding.time.minute
            calendar[Calendar.SECOND] = 0

            Log.d("💔", "${binding.time.hour}:${binding.time.minute}")

            var t2 = binding.time.hour*60*60 + binding.time.minute*60

            if((t2 - t1) >= 900) {

                calendar2 = Calendar.getInstance()
                if (binding.time.minute < 15) {
                    calendar2[Calendar.HOUR_OF_DAY] = binding.time.hour - 1
                    calendar2[Calendar.MINUTE] = 60 + (binding.time.minute - 15)
                    calendar2[Calendar.SECOND] = 0
                } else {
                    calendar2[Calendar.HOUR_OF_DAY] = binding.time.hour
                    calendar2[Calendar.MINUTE] = (binding.time.minute - 15)
                    calendar2[Calendar.SECOND] = 0
                }

                Log.d("💔", "${calendar2[Calendar.HOUR_OF_DAY]}:${calendar2[Calendar.MINUTE]}")

                alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

                val intent = Intent(this, AlarmReceiver::class.java)

                pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar2.getTimeInMillis(),
                    900000, // Repeat every 15 mins
                    pendingIntent)

                Toast.makeText(this, "Reminder Set", Toast.LENGTH_SHORT).show()

            }else{
                alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

                val intent = Intent(this, AlarmReceiver::class.java)

                pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.timeInMillis, pendingIntent)

                Toast.makeText(this, "Reminder Set", Toast.LENGTH_SHORT).show()

            }
            /**
            calendar2 = Calendar.getInstance()
            if(binding.time.minute < 15){
                calendar2[Calendar.HOUR_OF_DAY] = binding.time.hour - 1
                calendar2[Calendar.MINUTE] = 60 + (binding.time.minute - 15)
                calendar2[Calendar.SECOND] = 0
            }
            calendar2[Calendar.HOUR_OF_DAY] = binding.time.hour
            calendar2[Calendar.MINUTE] = (binding.time.minute - 15)
            calendar2[Calendar.SECOND] = 0
            **/



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
    fun createRem(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("rem", "rem", NotificationManager.IMPORTANCE_HIGH)
            channel.description = binding.event.text.toString()
            val notifM = getSystemService(NotificationManager::class.java)

            notifM.createNotificationChannel(channel)
        }
    }
}