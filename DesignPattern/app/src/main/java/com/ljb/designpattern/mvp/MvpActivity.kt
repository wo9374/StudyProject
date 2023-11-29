package com.ljb.designpattern.mvp

import android.os.Bundle
import com.ljb.designpattern.R
import com.ljb.base.BaseActivity
import com.ljb.designpattern.databinding.ActivityMvpBinding

class MvpActivity: BaseActivity<ActivityMvpBinding>(R.layout.activity_mvp) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }
}