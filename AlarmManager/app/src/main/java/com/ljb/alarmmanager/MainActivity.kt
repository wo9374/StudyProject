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
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ljb.alarmmanager.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val hourArray = Array(25) {i -> i.toString()}       //스피너 0 ~ 24를 위한 25
        val minuteArray = Array(61) {i -> i.toString()}     //스피너 0 ~ 60을 위한 61

        binding.apply {
            spinnerHour.adapter = ArrayAdapter(this@MainActivity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, hourArray)
            spinnerMinute.adapter = ArrayAdapter(this@MainActivity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, minuteArray)
            spinnerSecond.adapter = ArrayAdapter(this@MainActivity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, minuteArray)
        }


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

    private fun setOnClick() {
        binding.apply {
            btnRepeat.setOnClickListener { setRepeatAlarm(60) }
            btnSingle.setOnClickListener { setSingleDateAlarm() }

            switchAlarmClock.setOnCheckedChangeListener { compoundButton, isChechked ->
                if (isChechked)
                    switchAlarmClock.text = "켜짐"
                else
                    switchAlarmClock.text = "꺼짐"

                switchAlarmClock(isChechked)
            }
        }
    }

    private fun switchAlarmClock(switchOnOff: Boolean){
        val hour = binding.spinnerHour.selectedItemPosition
        val minute = binding.spinnerMinute.selectedItemPosition
        val second = binding.spinnerSecond.selectedItemPosition

        val receiverIntent = Intent(this@MainActivity, AlarmBroadcastReceiver::class.java).apply {
            putExtra(REQUEST_CODE, REQUEST_AlARM_CLOCK)
            putExtra(INTERVAL_HOUR, hour)
            putExtra(INTERVAL_MINUTE, minute)
            putExtra(INTERVAL_SECOND, second)
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this@MainActivity, PENDING_REPEAT_CLOCK, receiverIntent, Intent.FILL_IN_DATA or PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(this@MainActivity, PENDING_REPEAT_CLOCK, receiverIntent, Intent.FILL_IN_DATA or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        if (switchOnOff){
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                add(Calendar.HOUR_OF_DAY, hour)
                add(Calendar.MINUTE, minute)
                add(Calendar.SECOND, second)
            }
            val alarmClock = AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent)
            alarmManager.setAlarmClock(alarmClock, pendingIntent)
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun setRepeatAlarm(second: Int) {
        if (second < 60)
            Toast.makeText(this, "setInexactRepeating의\nInterval 최소 주기는 1분으로 실행됩니다.", Toast.LENGTH_SHORT).show()

        val receiverIntent = Intent(this, AlarmBroadcastReceiver::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, REQUEST_REPEAT, receiverIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(this, REQUEST_REPEAT, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }


        //Android 5.1(API 22)부터 배터리 소모량 등을 문제로 알람 반복은 최소 1분의 Interval을 갖도록 되었음
        //최초 한번 수행 후 1분보다 적은 interval 은 1분 주기로 수행
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime(),
            second * 1000L,
            pendingIntent
        )
    }


    private fun setSingleDateAlarm() {
        val receiverIntent = Intent(this, AlarmBroadcastReceiver::class.java)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, REQUEST_SINGLE, receiverIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(this, REQUEST_SINGLE, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 53)
            set(Calendar.SECOND, 0)

            // 지나간 시간의 경우 다음날 알람으로 울리도록
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1) // 하루 더하기
            }
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        //하루 간격 반복
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    private fun setSingleDateStringAlarm(pendingIntent: PendingIntent) {
        val hour = "5"
        val minute = "53"
        val second = "30"
        val time = "2000-01-01 $hour:$minute:$second" // 알람이 울리는 시간

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")
        val localDateTime = LocalDateTime.parse(time, formatter)

        val calendar = Calendar.getInstance()
        calendar.time = localDateTime.toDate()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}

