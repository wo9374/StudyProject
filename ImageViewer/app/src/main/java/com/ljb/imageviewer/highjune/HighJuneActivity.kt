package com.ljb.imageviewer.highjune

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ljb.imageviewer.circularProgress
import com.ljb.imageviewer.setUrlImgView

class HighJuneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageView = TouchImageView(this)
        setUrlImgView(
            this,
            "https://picsum.photos/1080/1920",
            imageView,
            circularProgress(this)
        )

        setContentView(imageView)

        /**
         * https://picsum.photos/2160/3840
         * https://picsum.photos/id/237/200/300       (특정 Id 이미지)
         * https://picsum.photos/seed/picsum/200/300 (Static Random Image)
         * https://random.imagecdn.app/3840/2160
         * */
    }
}