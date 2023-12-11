package com.ljb.notification

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.Person

const val CHANNEL_ID_SILENT = "silent-channel"
const val CHANNEL_ID_SOUND = "sound-channel"
const val CHANNEL_ID_VIBE = "vibration-channel"
const val CHANNEL_ID_SOUND_VIBE = "sound-vibration-channel"


private val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
private val audio: AudioAttributes = AudioAttributes.Builder()
    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
    .setUsage(AudioAttributes.USAGE_ALARM)
    .build()

fun NotificationCompat.Builder.setNotificationInfo(title: String, subText: String, contentIntent: PendingIntent) : NotificationCompat.Builder {
    setSmallIcon(R.drawable.ic_launcher_foreground)
    setWhen(System.currentTimeMillis())

    setContentTitle(title)
    setContentText(subText)
    setContentIntent(contentIntent)

    return this
}

fun createChannel(channelId: String, channelName:String, importance: Int) : NotificationChannel
= NotificationChannel(channelId, channelName,importance).apply{
    setShowBadge(true)
    enableLights(true)
    lightColor = Color.BLUE

    when(channelId){
        CHANNEL_ID_SOUND -> {
            setSound(uri, audio)
        }
        CHANNEL_ID_SOUND_VIBE -> {
            setSound(uri, audio)
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 100, 200)
        }
    }
}