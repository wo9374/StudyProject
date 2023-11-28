package com.ljb.cleanarchitecture_koin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ljb.cleanarchitecture.databinding.ActivityMainBinding
import com.ljb.cleanarchitecture_koin.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()
    private val listAdapter = NumberListAdapter()
    private val random = Random

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            rvList.adapter = listAdapter

            btnAdd.setOnClickListener {
                val num = generateRandomNumber()
                viewModel.insertNumber(num)
            }

            btnClear.setOnClickListener { viewModel.clearNumbers() }
        }

        lifecycleScope.launch {
            // viewModel이 가지는 flow
            viewModel.numbers
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .distinctUntilChanged()
                .collectLatest {
                    listAdapter.submitList(it)
                }
        }
    }

    /**
     * 랜덤한 숫자를 생성하는 함수
     */
    private fun generateRandomNumber() = random.nextInt(0..Int.MAX_VALUE)
}