package com.ljb.imageviewer.udarawanasinghe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ljb.imageviewer.circularProgress
import com.ljb.imageviewer.databinding.ActivityUdarawanasingheBinding
import com.ljb.imageviewer.setUrlImgView

/**
 * https://github.com/UdaraWanasinghe/android-transform-layout
 * */

class UdaraWanasingheActivity : AppCompatActivity() {

    private val binding by lazy { ActivityUdarawanasingheBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setUrlImgView(
            this,
            "https://random.imagecdn.app/3840/2160",
            binding.innerImgView,
            circularProgress(this)
        )
    }
}