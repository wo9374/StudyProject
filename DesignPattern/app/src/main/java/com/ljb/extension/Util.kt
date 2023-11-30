package com.ljb.extension

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.util.TypedValue
import android.view.View



sealed class UiState<out T> {
    data object Empty : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Complete<out T>(val data: T) : UiState<T>()
    data class Fail(val message: String?) : UiState<Nothing>()
}

fun String.htmlToString() : String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
    {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        return Html.fromHtml(this).toString()
    }
}


val Int.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()



fun View.setVisibility(boolean: Boolean) {
    if (boolean)
        this.visibility = View.VISIBLE
    else
        this.visibility = View.INVISIBLE
}