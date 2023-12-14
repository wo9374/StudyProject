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
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.ljb.alarmmanager.AlarmConst.INTERVAL_HOUR
import com.ljb.alarmmanager.AlarmConst.INTERVAL_MINUTE
import com.ljb.alarmmanager.AlarmConst.INTERVAL_SECOND
import com.ljb.alarmmanager.AlarmConst.PENDING_REPEAT
import com.ljb.alarmmanager.AlarmConst.PENDING_REPEAT_CLOCK
import com.ljb.alarmmanager.AlarmConst.PENDING_SINGLE_DATE
import com.ljb.alarmmanager.AlarmConst.REQUEST_CODE
import com.ljb.alarmmanager.AlarmConst.getPendingIntent
import com.ljb.alarmmanager.AlarmConst.requestAlarmClock
import com.ljb.alarmmanager.AlarmConst.requestDate
import com.ljb.alarmmanager.AlarmConst.requestRepeat
import com.ljb.alarmmanager.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val hourArray = Array(25) { i -> i.toString() }       //스피너 0 ~ 24를 위한 25
        val minuteArray = Array(61) { i -> i.toString() }     //스피너 0 ~ 60을 위한 61

        binding.apply {
            spinnerHour.adapter = ArrayAdapter(this@MainActivity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, hourArray)
            spinnerMinute.adapter = ArrayAdapter(this@MainActivity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, minuteArray)
            spinnerSecond.adapter = ArrayAdapter(this@MainActivity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, minuteArray)

            //DatePicker 내일 날짜를 최소 날짜 set
            datePicker.minDate = LocalDateTime.now().plusDays(1).toDate().time
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

    override fun onDestroy() {
        super.onDestroy()

        //Alarm 모두 취소
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            alarmManager.cancelAll()
        } else {
            alarmManager.cancel(getPendingIntent(this, PENDING_REPEAT_CLOCK))
            alarmManager.cancel(getPendingIntent(this, PENDING_SINGLE_DATE))
            alarmManager.cancel(getPendingIntent(this, PENDING_REPEAT))
        }
    }

    private fun setOnClick() {
        binding.apply {
            switchAlarmClock.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked)
                    switchAlarmClock.text = "켜짐"
                else
                    switchAlarmClock.text = "꺼짐"

                switchAlarmClock(isChecked)
            }

            btnDate.setOnClickListener { setDateAlarm() }

            switchRepeat.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked)
                    switchRepeat.text = "켜짐"
                else
                    switchRepeat.text = "꺼짐"

                setRepeatAlarm(isChecked)
            }
        }
    }

    private fun switchAlarmClock(switchOnOff: Boolean) {
        val hour = binding.spinnerHour.selectedItemPosition
        val minute = binding.spinnerMinute.selectedItemPosition
        val second = binding.spinnerSecond.selectedItemPosition

        val receiverIntent = Intent(this@MainActivity, AlarmBroadcastReceiver::class.java).apply {
            putExtra(REQUEST_CODE, requestAlarmClock)
            putExtra(INTERVAL_HOUR, hour)
            putExtra(INTERVAL_MINUTE, minute)
            putExtra(INTERVAL_SECOND, second)
        }
        val pendingIntent = getPendingIntent(this, PENDING_REPEAT_CLOCK, receiverIntent)

        if (switchOnOff) {
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

    private fun setDateAlarm() {
        val receiverIntent = Intent(this, AlarmBroadcastReceiver::class.java).apply {
            putExtra(REQUEST_CODE, requestDate)
        }
        val pendingIntent = getPendingIntent(this, PENDING_SINGLE_DATE, receiverIntent)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.YEAR, binding.datePicker.year)
            set(Calendar.MONTH, binding.datePicker.month)
            set(Calendar.DAY_OF_MONTH, binding.datePicker.dayOfMonth)

            //정각
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)

            /*// 지나간 시간의 경우 다음날 알람으로 울리도록
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1) // 하루 더하기
            }*/
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    private fun setDateStringAlarm(pendingIntent: PendingIntent) {
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

    //Android 5.1(API 22)부터 배터리 소모량 등을 문제로 알람 반복은 최소 1분의 Interval을 갖도록 되었음
    //최초 한번 수행 후 1분보다 적은 interval 은 1분 주기로 수행
    private fun setRepeatAlarm(switchOnOff: Boolean) {
        val receiverIntent = Intent(this, AlarmBroadcastReceiver::class.java).apply {
            putExtra(REQUEST_CODE, requestRepeat)
        }
        val pendingIntent = getPendingIntent(this, PENDING_REPEAT, receiverIntent)

        /**
         * AlarmManager.INTERVAL_DAY, INTERVAL_HALF_DAY, INTERVAL_HOUR, INTERVAL_HALF_HOUR, INTERVAL_FIFTEEN_MINUTES
         * */

        if (switchOnOff) {
            alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                60 * 1000L,
                pendingIntent
            )
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun setAlarmApplyByVersion() {
        val receiverIntent = Intent(this, AlarmBroadcastReceiver::class.java)
        val pendingIntent = getPendingIntent(this, PENDING_REPEAT, receiverIntent)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                    pendingIntent
                )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ->
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    AlarmManager.INTERVAL_HOUR,
                    pendingIntent
                )

            else ->
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
        }
    }
}

