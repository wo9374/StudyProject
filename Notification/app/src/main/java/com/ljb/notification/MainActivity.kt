package com.ljb.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import com.ljb.notification.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    companion object {
        const val DENIED = "denied"
        const val EXPLAINED = "explained"

        const val commonNotification = 0
        const val progressNotification = 1
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var manager: NotificationManager
    private lateinit var builder: NotificationCompat.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.apply {
            val silentChannel = createChannel(CHANNEL_ID_SILENT, "Silent", NotificationManager.IMPORTANCE_LOW)
            val soundChannel = createChannel(CHANNEL_ID_SOUND, "Sound", NotificationManager.IMPORTANCE_DEFAULT)
            val soundVibeChannel = createChannel(CHANNEL_ID_SOUND_VIBE, "SoundVibration", NotificationManager.IMPORTANCE_HIGH)

            createNotificationChannel(silentChannel)
            createNotificationChannel(soundChannel)
            createNotificationChannel(soundVibeChannel)
        }

        binding.btnNotify.setOnClickListener {
            //sdk 33 이후 버전은 따로 권한 요청 필요
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerForActivityResult.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
            } else {
                setNotify()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setNotify() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_SOUND_VIBE)
            .setNotificationInfo("알림", "그만하고 싶다", pendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Go to App", pendingIntent)

        //일반 notification
        manager.notify(commonNotification, builder.build())

        builder.setChannelId(CHANNEL_ID_SILENT)
            .setNotificationInfo("알림", "다되면 다 사라져 줘", pendingIntent)

        lifecycleScope.launch {
            for (i in 1..100) {
                builder.setProgress(100, i, false)
                manager.notify(progressNotification, builder.build())
                delay(100)
            }

            builder.setChannelId(CHANNEL_ID_SOUND_VIBE)
            manager.notify(progressNotification, builder.build())

            delay(2500)
            //manager.cancel(commonNotification)
            //manager.cancel(progressNotification)
            manager.cancelAll()
        }
    }

    // 권한 요청용 Activity Callback 객체
    private val registerForActivityResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
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

                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, applicationContext.packageName)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    }
                }

                else -> {
                    // 모든 권한이 허가 되었을 때
                    setNotify()
                }
            }
        }
}