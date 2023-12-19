package com.ljb.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MyBoundService : Service(){

    private val mBinder = MyBinder()

    private var count = 0
    private var job = CoroutineScope(Dispatchers.Default).launch {
        while (isActive){
            Log.e("Service Test", "BoundService 실행중")
            Log.e("Service Test", "$count")

            try {
                delay(2000)
                count++
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    inner class MyBinder : Binder() {
        fun getService() : MyBoundService {
            return this@MyBoundService
        }
    }

    override fun onBind(p0: Intent?): IBinder = mBinder

    override fun onCreate() {
        job.start()
    }

    //연결한 안드로이드 컴포넌트에서 사용할 함수
    fun getCount() = count

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        Log.e("Service Test", "onDestroy : job cancel")
    }
}