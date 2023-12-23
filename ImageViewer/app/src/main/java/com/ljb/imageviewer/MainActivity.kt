package com.ljb.imageviewer

import android.content.Intent
import android.os.Bundle
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.ljb.imageviewer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            btnTransLayout.setOnClickListener(onClick)
            btnTransImg.setOnClickListener(onClick)
            btnDoubletapPinch.setOnClickListener(onClick)
            btnZoomableImg.setOnClickListener(onClick)
        }
    }

    private val onClick = OnClickListener {
        binding.apply {
            val intent = Intent(
                applicationContext, when (it.id) {
                    btnTransLayout.id -> TransformLayoutActivity::class.java
                    btnTransImg.id -> TransformImageActivity::class.java
                    btnDoubletapPinch.id -> PinchDoubleTapActivity::class.java
                    btnZoomableImg.id -> ZoomableImageViewActivity::class.java
                    else -> MainActivity::class.java
                }
            )
            startActivity(intent)
        }
    }
}