package com.ljb.imageviewer

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import android.widget.Scroller
import androidx.appcompat.app.AppCompatActivity
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

    // 매트릭스 및 제스처 디텍터 변수
    private val matrix = Matrix()
    private lateinit var gestureDetector: GestureDetector
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    private lateinit var scroller: Scroller

    private var intrinsicWidth = 0f
    private var intrinsicHeight = 0f
    private val matrixValues = FloatArray(9)

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
                        Log.e("ZoomTest", String.format("bitmap %,d btyes, size: %d x %d", byteCount, width, height))
                    }
                }
                return false
            }
        }

        // Glide를 사용하여 이미지 로드
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



        // Gesture Detector 및 Scale Gesture Detector 초기화
        gestureDetector = GestureDetector(this@ZoomableImageViewActivity, GestureListener())
        scaleGestureDetector = ScaleGestureDetector(this@ZoomableImageViewActivity, ScaleListener())

        scroller = Scroller(this)



        // 이미지 뷰에 터치 리스너 설정
        binding.imgView.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)
            return@setOnTouchListener true
        }


        // 이미지 뷰의 ScaleType을 Matrix로 설정
        binding.imgView.scaleType = ImageView.ScaleType.MATRIX


        // 이미지 뷰에 초기 매트릭스 설정
        matrix.set(binding.imgView.imageMatrix)
        binding.imgView.imageMatrix = matrix
    }

    // Gesture Detector 리스너 구현
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            // Double Tap 시에 이미지를 2배로 확대
            val scale = if (matrix.isIdentity) 2.0f else 1.0f
            matrix.setScale(scale, scale, e.x, e.y)
            binding.imgView.imageMatrix = matrix
            Log.e("ZoomTest", "GestureListener - onDoubleTap()")
            return true
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            // Fling 동작 처리 (Scroller 사용)
            scroller.fling(
                matrixValues[Matrix.MTRANS_X].toInt(),
                matrixValues[Matrix.MTRANS_Y].toInt(),
                velocityX.toInt(),
                velocityY.toInt(),
                0,
                (binding.imgView.width - intrinsicWidth).toInt(),
                0,
                (binding.imgView.height - intrinsicHeight).toInt()
            )
            return true
        }
    }

    // Scale Gesture Detector 리스너 구현
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            // Pinch Zoom 동작 처리
            val scaleFactor = detector.scaleFactor
            matrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
            limitScale()
            binding.imgView.imageMatrix = matrix

            Log.e("ZoomTest", "ScaleListener - onScale()")
            return true
        }
    }

    // 이미지의 확대/축소를 제한하는 함수
    private fun limitScale() {
        matrix.getValues(matrixValues)
        val currentScale = matrixValues[Matrix.MSCALE_X]

        // 최소/최대 스케일을 정의
        val minScale = 1.0f
        val maxScale = 5.0f

        // 스케일 범위를 벗어나지 않도록 조정
        if (currentScale < minScale) {
            matrix.postScale(minScale / currentScale, minScale / currentScale, binding.imgView.width / 2f, binding.imgView.height / 2f)
        } else if (currentScale > maxScale) {
            matrix.postScale(maxScale / currentScale, maxScale / currentScale, binding.imgView.width / 2f, binding.imgView.height / 2f)
        }
    }

    // 이미지의 이동을 제한하는 함수
    private fun limitTranslation() {
        matrix.getValues(matrixValues)
        val transX = matrixValues[Matrix.MTRANS_X]
        val transY = matrixValues[Matrix.MTRANS_Y]

        // 이미지의 이동 범위를 정의
        val minX = binding.imgView.width - intrinsicWidth
        val minY = binding.imgView.height - intrinsicHeight
        val maxX = 0f
        val maxY = 0f

        // 이동 범위를 벗어나지 않도록 조정
        val adjustedTransX = transX.coerceIn(minX, maxX)
        val adjustedTransY = transY.coerceIn(minY, maxY)

        // 직접 값을 설정하여 현재 행렬에 적용
        matrix.setTranslate(adjustedTransX, adjustedTransY)
        binding.imgView.imageMatrix = matrix
    }
}