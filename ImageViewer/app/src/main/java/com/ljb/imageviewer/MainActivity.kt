package com.ljb.imageviewer

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ljb.imageviewer.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val circularProgress = CircularProgressDrawable(this).apply {
            strokeWidth = 5f        //선 두께
            centerRadius = 30f      //반지름
            start()
        }

        binding.transformLayout.apply {
            //default true
            //isRotateEnabled = false
            //isFlingEnabled = false

            setUrlImgView(
                this@MainActivity,
                "https://picsum.photos/id/20/900/700",
                binding.innerImgView,
                circularProgress
            )
        }

        binding.transformImgView.apply {
            //isRotateEnabled = false
            //isFlingEnabled = false

            setUrlImgView(
                this@MainActivity,
                "https://picsum.photos/id/80/900/700",
                this,
                circularProgress
            )
        }

        setUrlImgView(
            this,
            "https://picsum.photos/id/140/900/700",
            binding.pinchDtapImgView,
            circularProgress
        )
    }

    private fun setUrlImgView(
        context: Context, url: String, imgView: ImageView, placeholder: CircularProgressDrawable? = null
    ){
        Glide
            .with(context)
            .load(url)
            .listener(getRequestGlideListener(imgView))
            .centerCrop()
            .placeholder(placeholder)
            .into(imgView)
    }

    private fun getRequestGlideListener(view: ImageView) : RequestListener<Drawable> {
        return object : RequestListener<Drawable>{
            //실패시 노트북 사진 출력
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                //RequestListener 는 내부적으로 Worker Thread 처리가 되기 때문에 UI 작업시 Main Thread 필요
                CoroutineScope(Dispatchers.Main).launch {
                    Glide
                        .with(this@MainActivity)
                        .load("https://picsum.photos/id/0/200/300")
                        .centerCrop()
                        .into(view)
                }
                return false
            }

            //이미지 바이트, 크기 확인
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                if (resource is BitmapDrawable){
                    resource.bitmap.run {
                        Log.e(
                            "Image Test", String.format("${view.id} bitmap %,d btyes, size: %d x %d",
                                byteCount,		// Resizing 된 img byte
                                width,			// 이미지 넓이
                                height			// 이미지 높이
                            )
                        )
                    }
                }
                return false
            }
        }
    }
}