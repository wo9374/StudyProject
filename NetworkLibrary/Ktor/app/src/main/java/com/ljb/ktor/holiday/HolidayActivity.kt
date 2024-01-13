package com.ljb.ktor.holiday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.ljb.ktor.R
import com.ljb.ktor.databinding.ActivityHolidayBinding
import com.ljb.ktor.news.NetworkState
import com.ljb.ktor.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HolidayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHolidayBinding

    private val repository = HolidayRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_holiday)

        getHoliday("2024", "")
    }

    private fun getHoliday(solYear: String, solMonth: String) = lifecycleScope.launch {
        repository.getHoliday(solYear, solMonth).collectLatest { network ->
            when(network){
                is NetworkState.Success ->
                    Log.d("MyTag", "data: ${network.data}")

                is NetworkState.ApiError ->
                    showToast("ApiError Code: ${network.errorCode}, ApiError Message: ${network.message}")

                is NetworkState.NetworkError ->
                    Log.d("MyTag", "NetworkError: ${network.throwable}")
            }
        }
    }
}