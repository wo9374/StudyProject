package com.ljb.alarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

const val SingleAlarmCode = 0
const val RepeatAlarmCode = 1

class AlarmBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmTest", "onReceive")
    }
}