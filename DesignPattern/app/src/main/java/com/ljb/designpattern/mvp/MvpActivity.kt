package com.ljb.designpattern.mvp

import android.os.Bundle
import com.ljb.base.BaseActivity
import com.ljb.designpattern.R
import com.ljb.designpattern.databinding.ActivityPatternsBinding

class MvpActivity: BaseActivity<ActivityPatternsBinding>(R.layout.activity_patterns) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }
}