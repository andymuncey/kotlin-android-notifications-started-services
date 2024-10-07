package com.tinyappco.notificationdemo

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper

class NotificationService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val title = intent?.getStringExtra("title") ?: ""
        val content = intent?.getStringExtra("content") ?: ""
        val delayMins = intent?.getLongExtra("delay", 0) ?: 0L

        val runner = Runnable {
            val notifier = Notifier(this)
            notifier.sendNotification(title, content)
        }

        val handler = Handler(Looper.getMainLooper())
        //TODO: multiply by 60 after testing
        handler.postDelayed(runner,1000*delayMins)

        return START_STICKY
    }
}