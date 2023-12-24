package com.ljb.imageviewer.udarawanasinghe

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.withMatrix
import com.ljb.imageviewer.R

class TransformImageView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.transformLayoutStyle,
    defStyleRes: Int = R.style.TransformLayoutStyle
) : AppCompatImageView(context, attrs, defStyleAttr), Transformable {

    var isTransformEnabled = false
    override var isTranslateEnabled
        get() = gestureDetector.isTranslateEnabled
        set(value) { gestureDetector.isTranslateEnabled = value }

    override var isScaleEnabled
        get() = gestureDetector.isScaleEnabled
        set(value) { gestureDetector.isScaleEnabled = value }

    override var isRotateEnabled
        get() = gestureDetector.isRotateEnabled
        set(value) { gestureDetector.isRotateEnabled = value }

    override var isFlingEnabled
        get() = gestureDetector.isFlingEnabled
        set(value) { gestureDetector.isFlingEnabled = value }

    override val pivotPoint: Pair<Float, Float> get() = gestureDetector.pivotPoint
    override val transformMatrix: Matrix get() = gestureDetector.transformMatrix
    override val inverseTransformMatrix: Matrix get() = gestureDetector.inverseTransformMatrix

    private val gestureDetectorListeners = mutableSetOf<TransformGestureDetectorListener>()
    private val gestureDetectorListener = object : TransformGestureDetectorListener {

        override fun onTransformStart(px: Float, py: Float, matrix: Matrix, gestureDetector: TransformGestureDetector) {
            super.onTransformStart(px, py, matrix, gestureDetector)
            invalidate()
            gestureDetectorListeners.forEach {
                it.onTransformStart(px, py, matrix, gestureDetector)
            }
        }

        override fun onTransformUpdate(px: Float, py: Float, oldMatrix: Matrix, newMatrix: Matrix, gestureDetector: TransformGestureDetector) {
            super.onTransformUpdate(px, py, oldMatrix, newMatrix, gestureDetector)
            invalidate()
            gestureDetectorListeners.forEach {
                it.onTransformUpdate(px, py, oldMatrix, newMatrix, gestureDetector)
            }
        }

        override fun onTransformComplete(px: Float, py: Float, matrix: Matrix, gestureDetector: TransformGestureDetector
        ) {
            super.onTransformComplete(px, py, matrix, gestureDetector)
            invalidate()
            gestureDetectorListeners.forEach {
                it.onTransformComplete(px, py, matrix, gestureDetector)
            }
        }

        override fun onSingleTap(px: Float, py: Float, gestureDetector: TransformGestureDetector): Boolean {
            super.onSingleTap(px, py, gestureDetector)
            invalidate()
            return gestureDetectorListeners.any { it.onSingleTap(px, py, gestureDetector) }
        }

        override fun onLongPress(px: Float, py: Float, gestureDetector: TransformGestureDetector): Boolean {
            super.onLongPress(px, py, gestureDetector)
            invalidate()
            return gestureDetectorListeners.any { it.onLongPress(px, py, gestureDetector) }
        }

    }
    private val gestureDetector = TransformGestureDetector(context).apply {
        setGestureDetectorListener(gestureDetectorListener)
    }

    init {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.TransformLayout,
            defStyleAttr,
            defStyleRes
        ).apply {
            isScaleEnabled = getBoolean(R.styleable.TransformLayout_scaleEnabled, true)
            isRotateEnabled = getBoolean(R.styleable.TransformLayout_rotateEnabled, true)
            isTranslateEnabled = getBoolean(R.styleable.TransformLayout_translateEnabled, true)
            isFlingEnabled = getBoolean(R.styleable.TransformLayout_flingEnabled, true)
            isTransformEnabled = getBoolean(R.styleable.TransformLayout_transformEnabled, true)
            recycle()
        }
    }
    override fun setTransform(matrix: Matrix, notify: Boolean): Boolean {
        return gestureDetector.setTransform(matrix, notify)
    }

    override fun setTransform(values: FloatArray, notify: Boolean): Boolean {
        return gestureDetector.setTransform(values, notify)
    }

    override fun setTransform(scaling: Float?, rotation: Float?, translation: Pair<Float, Float>?, pivot: Pair<Float, Float>?, notify: Boolean): Boolean {
        return gestureDetector.setTransform(scaling, rotation, translation, pivot, notify)
    }

    override fun concatTransform(matrix: Matrix, notify: Boolean) {
        gestureDetector.concatTransform(matrix, notify)
    }

    override fun concatTransform(values: FloatArray, notify: Boolean) {
        gestureDetector.concatTransform(values, notify)
    }

    override fun concatTransform(
        scaling: Float?,
        rotation: Float?,
        translation: Pair<Float, Float>?,
        pivot: Pair<Float, Float>?,
        notify: Boolean
    ) {
        gestureDetector.concatTransform(scaling, rotation, translation, pivot, notify)
    }

    override fun resetTransform(notify: Boolean): Boolean {
        return gestureDetector.resetTransform(notify)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Touch events will be dispatched here only if they were intercepted or
        // children didn't consume them.
        if (isTransformEnabled) {
            // Gesture detector will consume events only is transform is enabled.
            return gestureDetector.onTouchEvent(event)
        }
        return false
    }

    override fun onDraw(canvas: Canvas) {
        canvas.withMatrix(gestureDetector.transformMatrix) {
            super.onDraw(canvas)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // Touch events are dispatched to the child views and the parent view from here.
        if (!isTransformEnabled) {
            // If transform is disabled, we have to transform the events dispatched to children.
            ev.transform(gestureDetector.inverseTransformMatrix)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun addTransformGestureDetectorListener(listener: TransformGestureDetectorListener) {
        gestureDetectorListeners.add(listener)
    }

    fun removeTransformGestureDetectorListener(listener: TransformGestureDetectorListener) {
        gestureDetectorListeners.remove(listener)
    }

}