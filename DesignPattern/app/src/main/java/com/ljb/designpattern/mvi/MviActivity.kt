package com.ljb.designpattern.mvi

import android.os.Bundle
import com.ljb.designpattern.R
import com.ljb.base.BaseActivity
import com.ljb.designpattern.databinding.ActivityMviBinding

class MviActivity: BaseActivity<ActivityMviBinding>(R.layout.activity_mvi) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }
}