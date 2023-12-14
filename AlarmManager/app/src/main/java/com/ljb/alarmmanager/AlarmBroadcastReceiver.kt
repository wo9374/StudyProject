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
import com.ljb.alarmmanager.AlarmConst.INTERVAL_HOUR
import com.ljb.alarmmanager.AlarmConst.INTERVAL_MINUTE
import com.ljb.alarmmanager.AlarmConst.INTERVAL_SECOND
import com.ljb.alarmmanager.AlarmConst.PENDING_REPEAT_CLOCK

object AlarmConst {
    //Intent extra 에 사용할 const
    const val REQUEST_CODE = "request_code"
    const val INTERVAL_HOUR = "interval_hour"
    const val INTERVAL_MINUTE = "interval_minute"
    const val INTERVAL_SECOND = "interval_second"

    const val requestAlarmClock = 0
    const val requestDate = 1
    const val requestRepeat = 2

    //PendingIntent 를 판단할 RequestCode
    const val PENDING_REPEAT_CLOCK = 0
    const val PENDING_SINGLE_DATE = 1
    const val PENDING_REPEAT = 2

    //앱 종료시 Alarm Cancel 수행을 위한 requestCode 구분 PendingIntent return 함수
    fun getPendingIntent(context: Context, pendingType: Int, receiverIntent: Intent? = null): PendingIntent {
        val intent = receiverIntent ?: Intent(context, AlarmBroadcastReceiver::class.java)

        return when (pendingType) {
            PENDING_REPEAT_CLOCK -> {
                PendingIntent.getBroadcast(
                    context, PENDING_REPEAT_CLOCK, intent,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        Intent.FILL_IN_DATA or PendingIntent.FLAG_IMMUTABLE
                    else
                        Intent.FILL_IN_DATA or PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

            PENDING_SINGLE_DATE -> {
                PendingIntent.getBroadcast(
                    context, PENDING_SINGLE_DATE, intent,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        PendingIntent.FLAG_IMMUTABLE
                    else
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

            //PENDING_REPEAT
            else -> {
                PendingIntent.getBroadcast(
                    context, PENDING_REPEAT, intent,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        PendingIntent.FLAG_IMMUTABLE
                    else
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        }
    }
}

class AlarmBroadcastReceiver : BroadcastReceiver() {
    @SuppressLint("ScheduleExactAlarm")
    override fun onReceive(context: Context, intent: Intent) {

        //REQUEST_CODE 판별
        when (intent.extras?.getInt(AlarmConst.REQUEST_CODE)) {
            AlarmConst.requestDate -> {
                Log.d("AlarmTest", "onReceive:: REQUEST_DATE")
            }

            AlarmConst.requestRepeat -> {
                Log.d("AlarmTest", "onReceive:: REQUEST_REPEAT")
            }

            AlarmConst.requestAlarmClock -> {
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

                //Intent 그대로 사용 (MainActivity 에서 준 PendingIntent 의 requestCode 와 Flag 를 똑같이 맞춰야 cancel 수행됨)
                val pendingIntent = PendingIntent.getBroadcast(context, PENDING_REPEAT_CLOCK, intent,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        Intent.FILL_IN_DATA or PendingIntent.FLAG_IMMUTABLE
                    else
                        Intent.FILL_IN_DATA or PendingIntent.FLAG_UPDATE_CURRENT
                )

                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmClock = AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent)
                alarmManager.setAlarmClock(alarmClock, pendingIntent)

                Log.d("AlarmTest", "Receiver 내부 setAlarmClock ${hour}시간 ${minute}분 ${second}초 마다 반복")
            }
        }
    }
}