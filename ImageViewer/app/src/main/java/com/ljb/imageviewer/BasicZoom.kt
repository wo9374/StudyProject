package com.ljb.imageviewer

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector

class BasicZoom  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    var scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    var scaleFactor = 1.0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            scaleGestureDetector.onTouchEvent(event)
        }
        return true
    }

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor

            // 최소 1, 최대 2배
            scaleFactor = 1f.coerceAtLeast(scaleFactor.coerceAtMost(2.0f))

            this@BasicZoom.scaleX = scaleFactor
            this@BasicZoom.scaleY = scaleFactor

            return true
        }
    }
}