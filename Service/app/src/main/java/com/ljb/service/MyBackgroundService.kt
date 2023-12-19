package com.ljb.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class MyBackgroundService : Service() {

    private var job : Job? = null
    private var count = 0

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        job = CoroutineScope(Dispatchers.Default).launch {
            while (isActive){
                Log.e("Service Test", "BackgroundService 실행중")
                Log.e("Service Test", "$count")

                try {
                    delay(2000)
                    count++
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        Log.e("Service Test", "onDestroy : job cancel")
    }
}