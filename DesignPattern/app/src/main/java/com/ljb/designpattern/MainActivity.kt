package com.ljb.designpattern

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ljb.designpattern.base.BaseActivity
import com.ljb.designpattern.databinding.ActivityMainBinding
import com.ljb.designpattern.mvc.MvcActivity
import com.ljb.designpattern.mvi.MviActivity
import com.ljb.designpattern.mvp.MvpActivity
import com.ljb.designpattern.mvvm.MvvmActivity

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        binding.callBack = mainCallback
    }

    interface MainCallback{ fun onClick(view: View) }

    private val mainCallback = object : MainCallback{
        override fun onClick(view: View) {

            binding.apply {
                val intent = Intent(applicationContext, when(view.id){
                    btnMvc.id -> MvcActivity::class.java
                    btnMvp.id -> MvpActivity::class.java
                    btnMvvm.id -> MvvmActivity::class.java
                    btnMvi.id -> MviActivity::class.java
                    else -> { MainActivity::class.java}
                })
                startActivity(intent)
            }
        }
    }
}