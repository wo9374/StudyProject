package com.ljb.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.lifecycle.lifecycleScope
import com.ljb.notification.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    companion object {
        const val DENIED = "denied"
        const val EXPLAINED = "explained"

        const val commonNotify = 0
        const val progressNotification = 1
        const val pictureNotification = 2
        const val textNotification = 3
        const val inboxNotification = 4
        const val messageNotification = 5
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //sdk 27 (Android 8) 이상은 채널을 만들고 지정 필요
        NotificationManagerCompat.from(this).apply {
            val silentChannel = createChannel(CHANNEL_ID_SILENT, "Silent", NotificationManager.IMPORTANCE_LOW)
            val vibeChannel = createChannel(CHANNEL_ID_VIBE, "Vibe", NotificationManager.IMPORTANCE_HIGH)
            val soundVibeChannel = createChannel(CHANNEL_ID_SOUND_VIBE, "SoundVibration", NotificationManager.IMPORTANCE_HIGH)

            //NotificationManager 에 채널 생성, 이후 채널을 만들때 사용한 id로 알림 구분 지정
            createNotificationChannel(silentChannel)
            createNotificationChannel(vibeChannel)
            createNotificationChannel(soundVibeChannel)
        }

        //sdk 33 (Android 13) 이후 버전은 따로 알림 권한 요청 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            registerForActivityResult.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
        }

        binding.apply {
            btnCommon.setOnClickListener { setOnClick(it) }
            btnProgress.setOnClickListener { setOnClick(it) }
            btnPicture.setOnClickListener { setOnClick(it) }
            btnBigTxt.setOnClickListener { setOnClick(it) }
            btnInbox.setOnClickListener { setOnClick(it) }
            btnMessage.setOnClickListener { setOnClick(it) }
        }
    }

    private fun setOnClick(view: View) {
        //sdk 33 (Android 13) 이후 버전 && 권한 미허용 시
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
        {
            registerForActivityResult.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
        }
        else {
            //알림 클릭시 띄울 Intent
            val intent = Intent(
                this@MainActivity, MainActivity::class.java
            ).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK }
            //PendingIntent 생성
            //getActivity, getBroadcast, getService 등등
            val pendingIntent = PendingIntent.getActivity(
                this@MainActivity, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            binding.apply {
                when(view.id){
                    btnCommon.id -> setCommonNotification(pendingIntent)
                    btnProgress.id -> setProgressNotification(pendingIntent)
                    btnPicture.id -> setBigPictureNotification(pendingIntent)
                    btnBigTxt.id -> setBigTextNotification(pendingIntent)
                    btnInbox.id -> setInBoxNotification(pendingIntent)
                    btnMessage.id -> setMessageNotification(pendingIntent)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setCommonNotification(pendingIntent: PendingIntent){

        //최대 3개까지 유저 이벤트 액션 추가가 가능
        val actionBuild= NotificationCompat
            .Action
            .Builder(R.drawable.ic_launcher_foreground, "Action", pendingIntent)
            .build()

        //NotificationCompat.Builder 이용 사용할 채널 ID 지정
        val builder = NotificationCompat
            .Builder(this, CHANNEL_ID_SOUND_VIBE)
            .setNotificationInfo("Notification", "Default Notification", pendingIntent)
            .addAction(actionBuild)

        NotificationManagerCompat
            .from(this).notify(commonNotify, builder.build())
    }

    @SuppressLint("MissingPermission")
    private fun setProgressNotification(pendingIntent: PendingIntent){

        val builder = NotificationCompat
            .Builder(this, CHANNEL_ID_VIBE)
            .setNotificationInfo("Progress", "Progress Notification", pendingIntent)
            .setProgress(100, 0, false)

        NotificationManagerCompat
            .from(this).notify(progressNotification, builder.build())

        //Progress 진행할 동안 계속 알림 울리지 않기 위한 채널 switching
        builder.setChannelId(CHANNEL_ID_SILENT)

        lifecycleScope.launch {
            for (i in 1..100) {
                builder.setProgress(100, i, false)
                NotificationManagerCompat.from(this@MainActivity).notify(progressNotification, builder.build())
                delay(100)
            }

            //Progress 완료시 사운드 진동 알림 한번더
            builder.setChannelId(CHANNEL_ID_SOUND_VIBE)
            NotificationManagerCompat
                .from(this@MainActivity).notify(progressNotification, builder.build())
        }
    }

    @SuppressLint("MissingPermission")
    private fun setBigPictureNotification(pendingIntent: PendingIntent){

        //공식 문서는 사진 1mb 이하 사용 권장 (고용량 실험 결과 : MainThread 부하, 성능 저하, 에뮬은 SystemUI isn't responding)
        val picture = BitmapFactory
            .decodeResource(this.resources, R.drawable.ex_pic)

        val style = NotificationCompat
            .BigPictureStyle()
            .bigPicture(picture)

        val builder = NotificationCompat
            .Builder(this, CHANNEL_ID_SOUND_VIBE)
            .setNotificationInfo("BigPicture", "BigPicture Notification", pendingIntent)
            .setStyle(style)

        NotificationManagerCompat
            .from(this).notify(pictureNotification, builder.build())
    }

    @SuppressLint("MissingPermission")
    private fun setBigTextNotification(pendingIntent: PendingIntent){

        val style = NotificationCompat
            .BigTextStyle()
            .bigText(getString(R.string.lorem_ipsum))

        val builder = NotificationCompat
            .Builder(this, CHANNEL_ID_SOUND_VIBE)
            .setNotificationInfo("BigText", "BigText Notification", pendingIntent)
            .setStyle(style)

        NotificationManagerCompat
            .from(this).notify(textNotification, builder.build())
    }

    @SuppressLint("MissingPermission")
    private fun setInBoxNotification(pendingIntent: PendingIntent){

        val style = NotificationCompat.InboxStyle().addLine("inBox A").addLine("inBox B").addLine("inBox C")

        val builder = NotificationCompat
            .Builder(this, CHANNEL_ID_SOUND_VIBE)
            .setNotificationInfo("InBox", "InBox Notification", pendingIntent)
            .setStyle(style)

        NotificationManagerCompat
            .from(this).notify(inboxNotification, builder.build())
    }

    @SuppressLint("MissingPermission")
    private fun setMessageNotification(pendingIntent: PendingIntent){

        val person1 = Person.Builder().setName("Lee").build()
        val person2 = Person.Builder().setName("Kim").build()

        val msg1 = NotificationCompat
            .MessagingStyle
            .Message("Hi Kim", System.currentTimeMillis(), person1)

        val msg2 = NotificationCompat
            .MessagingStyle
            .Message("Oh Hi", System.currentTimeMillis(), person2)

        val style = NotificationCompat
            .MessagingStyle(person1)
            .addMessage(msg1)
            .addMessage(msg2)

        val builder = NotificationCompat
            .Builder(this, CHANNEL_ID_SOUND_VIBE)
            .setNotificationInfo("Message", "Message Notification", pendingIntent)
            .setStyle(style)

        NotificationManagerCompat
            .from(this).notify(messageNotification, builder.build())
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
                        Toast.makeText(this@MainActivity, "알림 권한을 허용해 주세요.", Toast.LENGTH_SHORT).show()
                    }
                    map[EXPLAINED]?.let {
                        // 권한 요청이 완전히 막혔을 때(주로 앱 상세 창 열기)
                        Toast.makeText(this@MainActivity, "알림 권한을 허용해 주세요.", Toast.LENGTH_SHORT).show()

                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, applicationContext.packageName)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    }
                }

                else -> { // 모든 권한이 허가 되었을 때
                    /**
                     * [setOnClickNotify]에서 자체적 처리
                     * */
                }
            }
        }
}