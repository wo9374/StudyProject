package com.ljb.designpattern.mvc

import android.os.Bundle
import com.ljb.designpattern.R
import com.ljb.base.BaseActivity
import com.ljb.designpattern.databinding.ActivityMvcBinding

class MvcActivity: BaseActivity<ActivityMvcBinding>(R.layout.activity_mvc) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }
}