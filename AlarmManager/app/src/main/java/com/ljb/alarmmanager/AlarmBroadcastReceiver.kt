package com.ljb.alarmmanager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.util.Log


const val REQUEST_CODE = "request_code"
const val REQUEST_SINGLE = 0
const val REQUEST_REPEAT = 1
const val REQUEST_AlARM_CLOCK = 2

const val INTERVAL_HOUR = "interval_hour"
const val INTERVAL_MINUTE = "interval_minute"
const val INTERVAL_SECOND = "interval_second"

const val PENDING_REPEAT_CLOCK= 0


class AlarmBroadcastReceiver : BroadcastReceiver() {
    @SuppressLint("ScheduleExactAlarm")
    override fun onReceive(context: Context, intent: Intent) {
        //REQUEST_CODE 판별
        when (intent.extras?.getInt(REQUEST_CODE)) {
            REQUEST_SINGLE -> {
                Log.d("AlarmTest", "onReceive:: REQUEST_SINGLE")
            }
            REQUEST_REPEAT -> {
                Log.d("AlarmTest", "onReceive:: REQUEST_REPEAT")
            }
            REQUEST_AlARM_CLOCK -> {
                Log.d("AlarmTest", "onReceive:: REQUEST_AlARM_CLOCK")

                //Intent 에 넘어온 Extra Data 있는지 확인 없으면 default value 0
                val hour = intent.getIntExtra(INTERVAL_HOUR, 0)
                val minute = intent.getIntExtra(INTERVAL_MINUTE, 0)
                val second = intent.getIntExtra(INTERVAL_SECOND, 0)

                //전부 Calendar 에 Set
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    add(Calendar.HOUR_OF_DAY, hour)
                    add(Calendar.MINUTE, minute)
                    add(Calendar.SECOND, second)
                }

                //Intent 그대로 사용
                val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getBroadcast(context, PENDING_REPEAT_CLOCK, intent, PendingIntent.FLAG_IMMUTABLE)
                } else {
                    PendingIntent.getBroadcast(context, PENDING_REPEAT_CLOCK, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                }

                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmClock = AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent)
                alarmManager.setAlarmClock(alarmClock, pendingIntent)

                Log.d("AlarmTest", "Receiver 내부 setAlarmClock ${hour}시간 ${minute}분 ${second}초 마다 반복")
            }
        }
    }
}