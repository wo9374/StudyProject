package com.ljb.imageviewer.mycustom

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ljb.imageviewer.circularProgress
import com.ljb.imageviewer.databinding.ActivityZoomableImageViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyCustomActivity : AppCompatActivity() {

    private val binding by lazy { ActivityZoomableImageViewBinding.inflate(layoutInflater) }
    private val imageView get() = binding.imgView

    private lateinit var scaleGestureDetector: ScaleGestureDetector // 핀치 줌 제스처 감지를 위한 객체
    private lateinit var gestureDetector: GestureDetector           // 탭 제스처 감지를 위한 객체

    // ImageView 에 적용할 변환 Matrix 및 제스처 처리에 사용할 변수
    private val matrix = Matrix()
    private val startPoint = PointF()
    private var originalScaleX = 0f
    private var originalScaleY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Glide 이미지 로딩 시 이벤트 처리를 위한 리스너
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                //RequestListener 는 내부적으로 Worker Thread 처리가 되기 때문에 UI 작업시 Main Thread 필요
                CoroutineScope(Dispatchers.Main).launch {
                    Glide
                        .with(this@MyCustomActivity)
                        .load("https://picsum.photos/id/0/900/1000")    //실패시 노트북 사진 출력
                        .centerCrop()
                        .into(imageView)
                }
                return false
            }

            //이미지 바이트, 크기 확인
            @SuppressLint("ClickableViewAccessibility")
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                if (resource is BitmapDrawable){
                    resource.bitmap.run {
                        Log.e("ZoomTest", String.format("bitmap %,d btyes, size: %d x %d", byteCount, width, height))

                        //줌 가능 상태 활성화
                        enableZoom()
                        originalScaleX = imageView.width.toFloat() / width
                        originalScaleY = imageView.height.toFloat() / height
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
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView)

        // 핀치 줌 제스처를 감지하는 객체 초기화
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        // 더블 탭 제스처를 감지하는 객체 초기화
        gestureDetector = GestureDetector(this, GestureListener())
    }

    // 이미지뷰에 확대/축소 기능을 활성화하는 함수
    @SuppressLint("ClickableViewAccessibility")
    private fun enableZoom() {
        imageView.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)

            when (event.action) {

                // 사용자가 터치를 시작할 때의 처리
                MotionEvent.ACTION_DOWN -> {
                    startPoint.set(event.x, event.y)
                }

                // 사용자가 터치한 상태에서 이동할 때의 처리
                MotionEvent.ACTION_MOVE -> {

                    // 이미지 이동 처리
                    val offsetX = event.x - startPoint.x
                    val offsetY = event.y - startPoint.y
                    matrix.postTranslate(offsetX, offsetY)
                    startPoint.set(event.x, event.y)

                    adjustMatrix()
                }
            }

            imageView.imageMatrix = matrix
            true
        }
    }

    // 확대/축소 제스처를 감지하는 리스너
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            matrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)

            adjustMatrix()

            imageView.imageMatrix = matrix
            return true
        }
    }

    //제스처 감지 리스너
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        // 더블 탭 체스처 감지
        override fun onDoubleTap(e: MotionEvent): Boolean {
            //줌이 안되어 있을시에만 더블 탭으로 확대
            if (!isZoomedIn()) {
                matrix.postScale(3f, 3f, e.x, e.y) //더블 탭할 때 3배로 확대
            } else {
                // 다시 더블 탭할 때 원본 크기로 복귀
                matrix.reset()
                matrix.setScale(originalScaleX, originalScaleY)
            }

            adjustMatrix()

            imageView.imageMatrix = matrix
            return true
        }
    }

    // 현재 스케일을 가져와 이미지 원본 스케일과 비교해 줌이 되어있는지 판단해 return 하는 함수
    private fun isZoomedIn(): Boolean {
        //현재 스케일 get
        val values = FloatArray(9)
        matrix.getValues(values)
        val currentScale = values[Matrix.MSCALE_X]

        //현재 스케일 이용하여 이미지 원본 스케일 보다 클때 true
        return currentScale > originalScaleX && currentScale > originalScaleY
    }

    // ImageView 의 Matrix 를 조정하여 Zoom 영역이 이미지 영역을 벗어나지 않도록 하는 함수
    private fun adjustMatrix() {
        val values = FloatArray(9)
        matrix.getValues(values)

        val scaleX = values[Matrix.MSCALE_X]
        val scaleY = values[Matrix.MSCALE_Y]

        // 현재 스케일이 원본 스케일보다 작아지지 않도록 처리
        values[Matrix.MSCALE_X] = scaleX.coerceAtLeast(originalScaleX)
        values[Matrix.MSCALE_Y] = scaleY.coerceAtLeast(originalScaleY)

        val drawable = imageView.drawable
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            val displayWidth = imageView.width
            val displayHeight = imageView.height

            // 이미지가 화면보다 크게 줌인된 경우 Zoom 영역을 이미지 영역 내로 조정
            val maxTransX = (bitmap.width * values[Matrix.MSCALE_X] - displayWidth).coerceAtLeast(0f)
            val maxTransY = (bitmap.height * values[Matrix.MSCALE_Y] - displayHeight).coerceAtLeast(0f)

            values[Matrix.MTRANS_X] = values[Matrix.MTRANS_X].coerceIn(-maxTransX, 0f)
            values[Matrix.MTRANS_Y] = values[Matrix.MTRANS_Y].coerceIn(-maxTransY, 0f)

            matrix.setValues(values)
        }
    }
}