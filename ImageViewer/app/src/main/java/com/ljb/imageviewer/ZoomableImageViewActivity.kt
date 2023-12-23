package com.ljb.imageviewer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import android.widget.OverScroller
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ljb.imageviewer.databinding.ActivityZoomableImageViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ZoomableImageViewActivity : AppCompatActivity() {

    private val binding by lazy { ActivityZoomableImageViewBinding.inflate(layoutInflater) }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val listener = object : RequestListener<Drawable> {
            //실패시 노트북 사진 출력
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                //RequestListener 는 내부적으로 Worker Thread 처리가 되기 때문에 UI 작업시 Main Thread 필요
                CoroutineScope(Dispatchers.Main).launch {
                    Glide
                        .with(this@ZoomableImageViewActivity)
                        .load("https://picsum.photos/id/0/900/1000")
                        .centerCrop()
                        .into(binding.imgView)
                }
                return false
            }

            //이미지 바이트, 크기 확인
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                if (resource is BitmapDrawable){
                    resource.bitmap.run {
                        Log.e("MyZoomTest", String.format("bitmap %,d btyes, size: %d x %d", byteCount, width, height))
                    }
                }
                return false
            }
        }

        Glide
            .with(this)
            .load("https://picsum.photos/1080/1920")
            .centerCrop()
            .placeholder(circularProgress(this))
            .timeout(7000)
            .listener(listener)
            .skipMemoryCache(true)                  //메모리캐시 건너띄기
            .diskCacheStrategy(DiskCacheStrategy.NONE)    //디스크캐시 건너띄기
            .into(binding.imgView)
    }
}