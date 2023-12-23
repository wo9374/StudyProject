package com.ljb.imageviewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ljb.imageviewer.tramsformlayout.TransformImageView

class TransformImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageView = TransformImageView(this)
        setUrlImgView(
            this,
            "https://picsum.photos/1080/1920",
            imageView,
            circularProgress(this)
        )

        setContentView(imageView)
    }
}