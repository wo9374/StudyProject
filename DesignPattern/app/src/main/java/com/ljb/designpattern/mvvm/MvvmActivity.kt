package com.ljb.designpattern.mvvm

import android.os.Bundle
import com.ljb.designpattern.R
import com.ljb.designpattern.base.BaseActivity
import com.ljb.designpattern.databinding.ActivityMvvmBinding

class MvvmActivity: BaseActivity<ActivityMvvmBinding>(R.layout.activity_mvvm) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }
}