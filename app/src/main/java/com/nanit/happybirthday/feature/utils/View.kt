package com.nanit.happybirthday.feature.utils

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
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

fun View.getBitmapFromView(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    draw(Canvas(bitmap))
    return bitmap
}

fun View.changeRadius(view: ImageView, windowManager: WindowManager) {
    val point = view.getLocationOnScreen()
    val rootLayoutParams = layoutParams as ConstraintLayout.LayoutParams
    rootLayoutParams.circleRadius =
        (windowManager.defaultDisplay.width - (point.x * 2)) / 2 -
                context.resources.getDimension(R.dimen.border_radius).toInt()
    layoutParams = rootLayoutParams
}

private fun View.getLocationOnScreen(): Point {
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    return Point(location[0], location[1])
}

fun <T> ImageView.setPhoto(colorBoarder: Int, photo: T) {
    this.loadCircularImage(
        photo,
        resources.getDimension(R.dimen.border_radius),
        ContextCompat.getColor(this.context, colorBoarder)
    )
}

fun <T> ImageView.loadCircularImage(
    model: T,
    borderSize: Float = 0F,
    borderColor: Int = Color.WHITE
) {
    Glide.with(context)
        .asBitmap()
        .load(model)
        .apply(RequestOptions.circleCropTransform())
        .into(object : BitmapImageViewTarget(this) {
            override fun setResource(resource: Bitmap?) {
                setImageDrawable(
                    resource?.run {
                        RoundedBitmapDrawableFactory.create(
                            resources,
                            if (borderSize > 0) {
                                createBitmapWithBorder(borderSize, borderColor)
                            } else {
                                this
                            }
                        ).apply {
                            isCircular = true
                        }
                    }
                )
            }
        })
}

private fun Bitmap.createBitmapWithBorder(borderSize: Float, borderColor: Int): Bitmap {
    val borderOffset = (borderSize * 2).toInt()
    val halfWidth = width / 2
    val halfHeight = height / 2
    val circleRadius = halfWidth.coerceAtMost(halfHeight).toFloat()
    val newBitmap = Bitmap.createBitmap(
        width + borderOffset,
        height + borderOffset,
        Bitmap.Config.ARGB_8888
    )

    val centerX = halfWidth + borderSize
    val centerY = halfHeight + borderSize

    val paint = Paint()
    val canvas = Canvas(newBitmap).apply {
        drawARGB(0, 0, 0, 0)
    }

    paint.isAntiAlias = true
    paint.style = Paint.Style.FILL
    canvas.drawCircle(centerX, centerY, circleRadius, paint)

    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, borderSize, borderSize, paint)

    paint.xfermode = null
    paint.style = Paint.Style.STROKE
    paint.color = borderColor
    paint.strokeWidth = borderSize
    canvas.drawCircle(centerX, centerY, circleRadius, paint)
    return newBitmap
}




