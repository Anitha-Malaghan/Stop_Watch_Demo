package com.example.stopwatchdemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.Timer
import java.util.TimerTask

class StopWatchService : Service() {

    private var timer: Timer? = null
    private var time = 0.0

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        time = intent?.getDoubleExtra(CURRENT_TIME, 0.0) ?: 0.0

        timer?.cancel()
        timer = Timer()

        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                time++

                val updateIntent = Intent(UPDATED_TIME).apply {
                    setPackage(packageName)
                    putExtra(CURRENT_TIME, time)
                }

                sendBroadcast(updateIntent)
            }
        }, 0, 1000)

        return START_STICKY
    }



    override fun onDestroy() {
        timer?.cancel()
        timer = null
        super.onDestroy()
    }

    companion object {
        const val CURRENT_TIME = "com.example.stopwatchdemo.CURRENT_TIME"
        const val UPDATED_TIME = "com.example.stopwatchdemo.UPDATED_TIME"
    }
}
