package com.nanit.happybirthday.feature.b_day

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.nanit.happybirthday.R
import com.nanit.happybirthday.feature.b_day.model.UI
import com.nanit.happybirthday.feature.main.model.Baby
import com.nanit.happybirthday.feature.main.model.DataType
import com.nanit.happybirthday.feature.main.model.PhotoType
import com.nanit.happybirthday.feature.utils.changeRadius
import com.nanit.happybirthday.feature.utils.getImgBitmap
import com.nanit.happybirthday.feature.utils.setOldBitmap
import com.nanit.happybirthday.feature.utils.setStatusBarColor
import kotlinx.android.synthetic.main.activity_birthday.*
import kotlinx.android.synthetic.main.old_item.*
import kotlinx.android.synthetic.main.photo_item.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class BirthdayActivity : AppCompatActivity() {

    private val bDayVM by viewModel<BirthdayViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthday)

        initUI(bDayVM.getUI())
        initListener()
    }

    private fun initListener() {
        backAction.setOnClickListener { onBackPressed() }
    }

    private fun initUI(ui: UI) {
        ui.apply {
            content.setBackgroundColor(ContextCompat.getColor(applicationContext, background))
            ivCamera.setImageDrawable(ContextCompat.getDrawable(applicationContext, takePhoto))
            ivBackground.setImageDrawable(ContextCompat.getDrawable(applicationContext, backgroundImg))
            ivPhoto.apply {
                setImageDrawable(ContextCompat.getDrawable(applicationContext, photoPlace))
                borderColor = ContextCompat.getColor(applicationContext, photoBoarder)
            }
            this@BirthdayActivity.setStatusBarColor(background)
        }

        getBaby()?.apply {
            tvName.text = getString(R.string.today_s_is, name.uppercase())
            ivOld.setOldBitmap(bDay)
            tvOld.text = when(dataType) {
                DataType.MONTH -> getString(R.string.s_old, dataType?.value)
                DataType.YEAR -> getString(R.string.s_old, dataType?.value)
                else -> ""
            }

            ivPhoto.apply {
                when(photoType) {
                    PhotoType.TAKE -> setImageBitmap(photoUri.getImgBitmap())
                    PhotoType.UPLOAD -> setImageURI(Uri.parse(photoUri))
                }
                post { changeRadius(ivCamera) }
            }
        }
    }

    private fun getBaby() = Gson().fromJson(intent.getStringExtra(BABY), Baby::class.java)

    companion object {
        private const val BABY = "baby"

        fun newIntent(context: Context, baby: Baby): Intent { 
            val intent = Intent(context, BirthdayActivity::class.java)
            intent.putExtra(BABY, Gson().toJson(baby))
            return intent
        }
    }
}