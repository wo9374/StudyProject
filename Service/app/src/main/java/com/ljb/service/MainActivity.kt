package com.ljb.service

import android.Manifest
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
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

    companion object {
        const val DENIED = "denied"
        const val EXPLAINED = "explained"
    }

    private lateinit var prefsManager : PrefsManager    //ForegroundService 실행 유무 Preference

    private lateinit var mBoundService : MyBoundService
    var isService = false

    // bindService() 메서드를 통해 시스템에 전달하면 서비스와 연결할 수 있음
    private val connection = object : ServiceConnection {
        // 서비스가 연결되면 호출
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MyBoundService.MyBinder
            mBoundService = binder.getService()
            isService = true    // onServiceDisconnected 구조에 때문에 서비스 연결 상태를 확인하는 로직이 필요
            Log.e("BoundService Test", "Connected")
        }

        // 정상적으로 연결 해제되었을 때는 호출되지 않고, 비정상적으로 서비스가 종료되었을 때만 호출
        override fun onServiceDisconnected(name: ComponentName?) {
            isService = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //sdk 33 (Android 13) 이후 버전은 따로 알림 권한 요청 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            registerForActivityResult.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
        }

        observePrefs()
        initialLayout()
    }

    private fun observePrefs(){
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
    }

    private fun initialLayout(){
        binding.apply {
            switchForeground.setOnCheckedChangeListener { compoundButton, isChecked ->

                lifecycleScope.launch {

                    val foregroundService = Intent(this@MainActivity, MyForegroundService::class.java)

                    if (isChecked){
                        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                            || Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU){
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
                    binding.switchBackground.text = "켜짐"
                } else {
                    stopService(backgroundService)
                    binding.switchBackground.text = "꺼짐"
                }
            }

            switchBound.setOnCheckedChangeListener { compoundButton, isChecked ->
                val intent = Intent(this@MainActivity, MyBoundService::class.java)

                if (isChecked){
                    //서비스를 호출하면서 커넥션을 같이 넘겨준다.
                    //BIND_AUTO_CREATE : 서비스가 생성되어 있지 않으면 생성 후 바인딩을 하고 생성되어 있으면 바로 바인딩
                    bindService(intent, connection, Context.BIND_AUTO_CREATE)
                    binding.switchBound.text = "켜짐"
                } else {
                    if (isService){
                        unbindService(connection)
                        isService = false
                    }
                    binding.switchBound.text = "꺼짐"
                }
            }

            btnCount.setOnClickListener {
                if (isService){
                    Toast.makeText(this@MainActivity, "BoundService Count: ${mBoundService.getCount()}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "BoundService가 연결되지 않았습니다.", Toast.LENGTH_SHORT).show()
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