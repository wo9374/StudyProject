package com.ljb.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.appcompat.app.AppCompatActivity
import com.ljb.alarmmanager.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var alarmManager : AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            when {
                // 권한이 부여된 경우 정확한 알람 예약을 진행
                alarmManager.canScheduleExactAlarms() -> setOnClick()

                // 사용자에게 시스템 설정의 정확한 알람 페이지로 이동하도록 요청
                else -> startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            }
        }
    }
    private fun setOnClick(){
        binding.apply {
            btnRepeat.setOnClickListener { setRepeatAlarm() }
            btnSingleDate.setOnClickListener { setSingleDateAlarm() }
        }
    }

    fun cancelAlarm(alarmCode : Int){
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent.getBroadcast(this, alarmCode, intent,PendingIntent.FLAG_IMMUTABLE)
        }else{
            PendingIntent.getBroadcast(this, alarmCode, intent,PendingIntent.FLAG_UPDATE_CURRENT)
        }
        alarmManager.cancel(pendingIntent)
    }

    private fun setRepeatAlarm(){
        val receiverIntent = Intent(this, AlarmBroadcastReceiver::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent.getBroadcast(this, RepeatAlarmCode, receiverIntent,PendingIntent.FLAG_IMMUTABLE)
        }else{
            PendingIntent.getBroadcast(this, RepeatAlarmCode, receiverIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        }


        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime(),
            10 * 1000L,
            pendingIntent
        )
        //최초 한번 수행 후 1분보다 적은 interval 은 1분 주기로 수행
        //Android 5.1(API 22)부터 배터리 소모량 등을 문제로 알람 반복은 최소 1분의 Interval을 갖도록 되었음
    }


    // api 23(android 6.0) 레벨부터 도즈모드 도입으로 setExact 사용 시 알람이 울리지 않음)
    private fun setSingleDateAlarm(){
        val receiverIntent = Intent(this, AlarmBroadcastReceiver::class.java)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent.getBroadcast(this, SingleAlarmCode, receiverIntent,PendingIntent.FLAG_IMMUTABLE)
        }else{
            PendingIntent.getBroadcast(this, SingleAlarmCode, receiverIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 53)
            //set(Calendar.SECOND, 30)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        //하루 간격 반복
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    private fun setSingleDateStringAlarm(pendingIntent: PendingIntent){
        val hour = "5"
        val minute = "53"
        val second = "30"
        val time = "2000-01-01 $hour:$minute:$second" // 알람이 울리는 시간

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")
        val localDateTime = LocalDateTime.parse(time, formatter)

        val calendar = Calendar.getInstance()
        calendar.time = localDateTime.toDate()
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}

