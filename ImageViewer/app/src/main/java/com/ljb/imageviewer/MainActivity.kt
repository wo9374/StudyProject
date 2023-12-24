package com.ljb.imageviewer

import android.content.Intent
import android.os.Bundle
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.ljb.imageviewer.databinding.ActivityMainBinding
import com.ljb.imageviewer.highjune.HighJuneActivity
import com.ljb.imageviewer.mycustom.MyCustomActivity
import com.ljb.imageviewer.udarawanasinghe.UdaraWanasingheActivity

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            btnUdarawanasinghe.setOnClickListener(onClick)
            btnHighjune.setOnClickListener(onClick)
            btnMyCustom.setOnClickListener(onClick)
        }
    }

    private val onClick = OnClickListener {
        binding.apply {
            val intent = Intent(
                applicationContext, when (it.id) {
                    btnUdarawanasinghe.id -> UdaraWanasingheActivity::class.java
                    btnHighjune.id -> HighJuneActivity::class.java
                    btnMyCustom.id -> MyCustomActivity::class.java
                    else -> MainActivity::class.java
                }
            )
            startActivity(intent)
        }
    }
}