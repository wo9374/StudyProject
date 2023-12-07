package com.ljb.datastore.preferences

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ljb.datastore.databinding.ActivityDatastoreBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Preferences DataStore 구현
 * */
class PreferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatastoreBinding

    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatastoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userManager = UserManager(dataStore)

        setUi()
        observeData()
    }

    private fun setUi() {
        binding.apply {
            btnSave.setOnClickListener {
                val name = etName.text.toString()
                val age = etAge.text.toString().toIntOrNull()
                val isMale = switchGender.isChecked

                if (name.isEmpty() || age == null) {
                    showToast("이름과 나이를 모두 입력하세요")
                } else {
                    CoroutineScope(Dispatchers.IO).launch { userManager.storeUser(age, name, isMale) }
                }
            }

            btnClear.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch { userManager.clearUser() }
            }
        }
    }

    private fun observeData() = lifecycleScope.launch {
        launch {
            userManager.userAgeFlow.collectLatest { binding.tvAge.text = it?.toString() ?: "" }
        }

        launch {
            userManager.userNameFlow.collectLatest { binding.tvName.text = it ?: "" }
        }

        launch {
            userManager.userGenderFlow.collectLatest {
                val gender = it?.let {
                    if (it) "여성" else "남성"
                } ?: ""
                binding.tvGender.text = gender
            }
        }
    }
}

fun Context.showToast(str: String?) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}