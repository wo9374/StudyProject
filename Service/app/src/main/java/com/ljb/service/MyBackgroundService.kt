package com.ljb.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyBackgroundService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.Default).launch {
            while (true){

                Log.e("Service Test", "BackgroundService 실행중")
                try {
                    delay(2000)
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }
}