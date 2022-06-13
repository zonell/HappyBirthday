package com.nanit.happybirthday.feature.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.nanit.happybirthday.R
import kotlinx.android.synthetic.main.take_photo_dialog.*

class DialogBuilder(context: Context) : Dialog(context) {

    fun build(): Dialog {
        val dialogView = Dialog(context, R.style.Base_Theme_AppCompat_Dialog_Alert)
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.window?.attributes?.gravity = Gravity.CENTER
        dialogView.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT

        return dialogView
    }

    fun showPhotoDialog(takePhoto:() -> Unit, uploadPhoto:() -> Unit) {
        DialogBuilder(context).build().apply {
            setContentView(R.layout.take_photo_dialog)
            tvTitle.text = context.getString(R.string.take_photo)
            tvDescription.text =
                context.getText(R.string.how_would_you_like_to_add_a_photo_upload_from_gallery_or_take_a_new_photo)
            btnNegative.apply {
                text = context.getString(R.string.take)
                setOnClickListener {
                    takePhoto()
                    dismiss()
                }
            }
            btnPositive.apply {
                text = context.getString(R.string.upload)
                setOnClickListener {
                    uploadPhoto()
                    dismiss()
                }
            }
        }.show()
    }

}