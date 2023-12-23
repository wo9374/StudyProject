package com.ljb.imageviewer

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun setUrlImgView(
    context: Context, url: String, imgView: ImageView, placeholder: CircularProgressDrawable? = null
){
    Glide
        .with(context)
        .load(url)
        .listener(getRequestGlideListener(context, imgView))
        .centerCrop()
        .placeholder(placeholder)
        .timeout(7000)

        //둘다 적용시 매번 새로운 이미지 load
        .skipMemoryCache(true)                  //메모리캐시 건너띄기
        .diskCacheStrategy(DiskCacheStrategy.NONE)    //디스크캐시 건너띄기

        .into(imgView)
}

fun getRequestGlideListener(context: Context, view: ImageView) : RequestListener<Drawable> {
    return object : RequestListener<Drawable> {
        //실패시 노트북 사진 출력
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

            //RequestListener 는 내부적으로 Worker Thread 처리가 되기 때문에 UI 작업시 Main Thread 필요
            CoroutineScope(Dispatchers.Main).launch {
                Glide
                    .with(context)
                    .load("https://picsum.photos/id/0/900/1000")
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

fun circularProgress(context: Context) = CircularProgressDrawable(context).apply {
    strokeWidth = 5f        //선 두께
    centerRadius = 30f      //반지름
    setColorSchemeColors(ContextCompat.getColor(context, com.google.android.material.R.color.design_default_color_on_primary))
    start()
}