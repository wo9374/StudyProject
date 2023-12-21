package com.ljb.imageviewer.tramsform.extensions

import android.view.MotionEvent
import android.view.ViewConfiguration

internal fun MotionEvent.focusPoint(): Pair<Float, Float> {
    val upIndex = if (actionMasked == MotionEvent.ACTION_POINTER_UP) actionIndex else -1
    var sumX = 0f
    var sumY = 0f
    var sumCount = 0
    for (pointerIndex in 0 until pointerCount) {
        if (pointerIndex == upIndex) continue
        sumX += getX(pointerIndex)
        sumY += getY(pointerIndex)
        sumCount++
    }
    val focusX = sumX / sumCount
    val focusY = sumY / sumCount
    return focusX to focusY
}

object ViewConfigurationCompatExtended {

    @Suppress("DEPRECATION")
    private var touchSlop = ViewConfiguration.getTouchSlop()

    fun setTouchSlop(touchSlop: Int) {
        this.touchSlop = touchSlop
    }

    fun getTouchSlop(): Int {
        return touchSlop
    }

}