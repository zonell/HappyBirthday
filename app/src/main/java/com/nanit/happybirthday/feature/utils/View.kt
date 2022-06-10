package com.nanit.happybirthday.feature.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.nanit.happybirthday.R
import kotlinx.android.synthetic.main.activity_birthday.*
import kotlinx.android.synthetic.main.photo_item.*

fun View?.gone() {
    this?.let { visibility = View.GONE }
}

fun View?.visible() {
    this?.let { visibility = View.VISIBLE }
}

fun View?.enable() {
    this?.let { isEnabled = true }
}

fun View?.disable() {
    this?.let { isEnabled = false }
}

fun View?.invisible() {
    this?.let { visibility = View.INVISIBLE }
}

fun View?.visibleIf(predicate: () -> Boolean) {
    when (predicate.invoke()) {
        true -> visible()
        else -> gone()
    }
}

fun View?.enableIf(predicate: () -> Boolean) {
    when (predicate.invoke()) {
        true -> enable()
        else -> disable()
    }
}

fun View?.hideKeyboard() {
    this?.let {
        val imm: InputMethodManager? =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun ImageView?.setOldBitmap(old: String) {
    this?.apply {
        val id = this.resources.getIdentifier("ic_${old}_old", "drawable", this.context.packageName)
        this.setImageResource(id)
    }
}

fun Activity?.setStatusBarColor(color: Int) {
    this?.apply {
        window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = ContextCompat.getColor(this@setStatusBarColor, color)
        }
    }
}

fun View.changeRadius(view: View) {
    val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
    layoutParams.circleRadius = this.width.dp*2 - view.width.dp*2
    view.layoutParams = layoutParams
}

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()




