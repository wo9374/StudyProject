package com.ljb.imageviewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ljb.imageviewer.databinding.ActivityTransformLayoutBinding

class TransformLayoutActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTransformLayoutBinding.inflate(layoutInflater) }
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