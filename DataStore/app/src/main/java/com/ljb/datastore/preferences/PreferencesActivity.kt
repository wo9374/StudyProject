package com.ljb.datastore.preferences

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.ljb.datastore.databinding.ActivityPreferencesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

//https://onlyfor-me-blog.tistory.com/519

/**
 * Preferences DataStore 구현
 * */
class PreferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreferencesBinding

    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userManager = UserManager(dataStore)

        setUi()
        observeData()
    }

    private fun setUi() {
        binding.apply {
            btnPreference.setOnClickListener {
                val name = etName.text.toString()
                val age = etAge.text.toString().toIntOrNull()
                val isMale = switchGender.isChecked

                if (name.isEmpty() || age == null) {
                    showToast("이름과 나이를 모두 입력하세요")
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        userManager.storeUser(age, name, isMale)
                    }
                }
            }
        }
    }

    private fun observeData() = lifecycleScope.launch {
        launch {
            userManager.userAgeFlow.collectLatest {
                it?.let { binding.tvAge.text = it.toString() }
            }
        }

        launch {
            userManager.userNameFlow.collectLatest {
                it?.let { binding.tvName.text = it }
            }
        }

        launch {
            userManager.userGenderFlow.collectLatest {
                it?.let { binding.tvGender.text = if (it) "여성" else "남성" }
            }
        }
    }
}

/**
 * by keyword 사용 DataStore<Preferences> 구현
 * preferencesDataStore()에 맡기는 Context 확장 Property
 *
 * 하위에 타입 별로 DataStore 에 저장하거나 가져오는 함수를 구현해도 되지만 여기선 이것만 사용한다.
 * */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

fun Context.showToast(str: String?) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}