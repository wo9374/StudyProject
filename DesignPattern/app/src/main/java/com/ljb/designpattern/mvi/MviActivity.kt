package com.ljb.designpattern.mvi

import android.os.Bundle
import com.ljb.base.BaseActivity
import com.ljb.designpattern.R
import com.ljb.designpattern.databinding.ActivityPatternsBinding

class MviActivity: BaseActivity<ActivityPatternsBinding>(R.layout.activity_patterns) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }
}