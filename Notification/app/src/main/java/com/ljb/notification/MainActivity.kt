package com.ljb.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.ljb.notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val DENIED = "denied"
        const val EXPLAINED = "explained"

        const val channelId = "one-channel"
        const val channelName = "My One Channel"
        const val notifyImportant = NotificationManager.IMPORTANCE_HIGH
    }

    private lateinit var binding: ActivityMainBinding

    lateinit var manager: NotificationManager
    lateinit var builder : NotificationCompat.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = createPendingIntent(this)


        // SDK 26 이상은 notification 만들 때 channel 지정
        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = createChannel()
            manager.createNotificationChannel(channel)
            NotificationCompat.Builder(this, channelId)
        } else {
            NotificationCompat.Builder(this)
        }.apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setWhen(System.currentTimeMillis())
            setContentTitle("알림")
            setContentText("알림 테스트")
            setAutoCancel(false)        //전체 삭제해도 남아있게
            setOngoing(true)            //알람이 계속 뜬 상태로 있게
            setContentIntent(pendingIntent) // notification 클릭 시 pendingIntent 오픈
        }

        binding.btnNotify.setOnClickListener {
            //sdk 33 이후 버전은 따로 권한 요청 필요
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerForActivityResult.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
            }else{
                manager.notify(0, builder.build())
            }
        }
    }

    //MainActivity PendingIntent 생성해 return
    private fun createPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        return PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createChannel() = NotificationChannel(channelId, channelName, notifyImportant).apply {
        description = "My Channel One Description"
        setShowBadge(true)

        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val audio = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        setSound(uri, audio)

        enableLights(true)
        lightColor = Color.RED

        enableVibration(true)
        vibrationPattern = longArrayOf(100, 200, 100, 200)
    }

    // 권한 요청용 Activity Callback 객체
    private val registerForActivityResult = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val deniedPermissionList = permissions.filter { !it.value }.map { it.key }
        when {
            deniedPermissionList.isNotEmpty() -> {
                val map = deniedPermissionList.groupBy { permission ->
                    if (shouldShowRequestPermissionRationale(permission)) DENIED else EXPLAINED
                }
                map[DENIED]?.let {
                    // 단순히 권한이 거부 되었을 때
                    Toast.makeText(this, "권한이 거부 되어 기능 실행이 불가 합니다.", Toast.LENGTH_SHORT).show()
                }
                map[EXPLAINED]?.let {
                    // 권한 요청이 완전히 막혔을 때(주로 앱 상세 창 열기)
                    Toast.makeText(this, "권한이 거부 되어 기능 실행이 불가 합니다.", Toast.LENGTH_SHORT).show()

                    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, applicationContext.packageName)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    }else{
                        Intent().apply {
                            action = "android.settings.APP_NOTIFICATION_SETTINGS"
                            putExtra("app_package", applicationContext.packageName)
                            putExtra("app_uid", applicationContext.applicationInfo.uid)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    }
                    startActivity(intent)
                }
            }
            else -> {
                // 모든 권한이 허가 되었을 때
                manager.notify(0, builder.build())
            }
        }
    }
}