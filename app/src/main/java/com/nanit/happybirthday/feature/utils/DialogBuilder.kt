package com.nanit.happybirthday.feature.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.nanit.happybirthday.R

class DialogBuilder(context: Context) : Dialog(context) {

    fun build(): Dialog {
        val dialogView = Dialog(context, R.style.Base_Theme_AppCompat_Dialog_Alert)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.window?.attributes?.gravity = Gravity.CENTER
        dialogView.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT

        return dialogView
    }

}