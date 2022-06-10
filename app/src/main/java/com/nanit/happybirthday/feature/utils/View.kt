package com.nanit.happybirthday.feature.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

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
        val imm: InputMethodManager? = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}




