package com.ljb.service

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ljb.service.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//getRunningServices 를 사용한 Service 실행 확인
fun <T> Context.isServiceRunning(service: Class<T>): Boolean {
    return (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE) // 작동중인 서비스 수 만큼 반복
        .any { it.service.className == service.name } //확인할 서비스 이름과 같다면
}

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var prefsManager : PrefsManager

    companion object {
        const val DENIED = "denied"
        const val EXPLAINED = "explained"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //sdk 33 (Android 13) 이후 버전은 따로 알림 권한 요청 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            registerForActivityResult.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
        }

        prefsManager = PrefsManager(this.dataStore)
        lifecycleScope.launch {
            prefsManager.foregroundRunningFlow.collectLatest {
                binding.switchForeground.apply {
                    isChecked = it ?: false

                    text = if (it == true){
                        "켜짐"
                    } else {
                        "꺼짐"
                    }
                }
            }
        }

        binding.apply {
            switchForeground.setOnCheckedChangeListener { compoundButton, isChecked ->

                lifecycleScope.launch {

                    val foregroundService = Intent(this@MainActivity, MyForegroundService::class.java)

                    if (isChecked){
                        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
                            startService(foregroundService)

                            withContext(Dispatchers.Default){
                                prefsManager.setService(true)
                            }
                        }
                    } else {
                        stopService(foregroundService)
                        withContext(Dispatchers.Default){
                            prefsManager.setService(false)
                        }
                    }
                }
            }

            switchBackground.setOnCheckedChangeListener { compoundButton, isChecked ->
                val backgroundService = Intent(this@MainActivity, MyBackgroundService::class.java)
                if (isChecked){
                    startService(backgroundService)
                } else {
                    stopService(backgroundService)
                }
            }
        }
    }

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
                        Toast.makeText(this@MainActivity, "ForegroundService 알림을 위해 알림 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                    }
                    map[EXPLAINED]?.let {
                        // 권한 요청이 완전히 막혔을 때(주로 앱 상세 창 열기)
                        Toast.makeText(this@MainActivity, "ForegroundService 알림을 위해 알림 권한이 필요합니다.", Toast.LENGTH_SHORT).show()

                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, applicationContext.packageName)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    }
                }

                else -> { // 모든 권한이 허가 되었을 때

                }
            }
        }
}