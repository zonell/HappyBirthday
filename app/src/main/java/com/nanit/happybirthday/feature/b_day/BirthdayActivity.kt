package com.nanit.happybirthday.feature.b_day

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.nanit.happybirthday.R
import com.nanit.happybirthday.feature.b_day.model.UI
import com.nanit.happybirthday.feature.main.model.Baby
import com.nanit.happybirthday.feature.main.model.DataType
import com.nanit.happybirthday.feature.takePhoto.TakePhotoActivity
import com.nanit.happybirthday.feature.utils.*
import kotlinx.android.synthetic.main.activity_birthday.*
import kotlinx.android.synthetic.main.activity_birthday.content
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.old_item.*
import kotlinx.android.synthetic.main.photo_item.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BirthdayActivity : AppCompatActivity() {

    private val bDayVM by viewModel<BirthdayViewModel>()
    private var colorBoarder: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthday)

        initUI(bDayVM.getUI())
        initListener()
    }

    private fun initListener() {
        backAction.setOnClickListener { onBackPressed() }
        ivCamera.setOnClickListener {
            DialogBuilder(this).showPhotoDialog({ takePhoto() }, { uploadPhoto() })
        }
    }

    private fun initUI(ui: UI) {
        ui.apply {
            content.setBackgroundColor(ContextCompat.getColor(applicationContext, background))
            ivCamera.setImageDrawable(ContextCompat.getDrawable(applicationContext, takePhoto))
            ivBackground.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    backgroundImg
                )
            )
            this@BirthdayActivity.setStatusBarColor(background)
            this@BirthdayActivity.colorBoarder = colorBoarder

            getBaby()?.apply {
                tvName.text = getString(R.string.today_s_is, name.uppercase())
                ivOld.setOldBitmap(bDay)
                tvOld.text = when (dataType) {
                    DataType.MONTH -> getString(R.string.s_old, dataType?.value)
                    DataType.YEAR -> getString(R.string.s_old, dataType?.value)
                    else -> ""
                }

                photoUri?.let {
                    ivPhoto.setPhoto(colorBoarder, it)
                } ?: kotlin.run {
                    ivPhoto.setPhoto(colorBoarder, photoPlace)
                }
            }
        }
    }

    private var takePhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    it.getStringExtra(TakePhotoActivity.PHOTO)?.apply {
                        colorBoarder?.let { color -> ivPhoto.setPhoto(color, this) }
                    }
                }
            }
        }

    private var uploadPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let {
                    colorBoarder?.let { color -> ivPhoto.setPhoto(color, it) }
                }
            }
        }

    private fun takePhoto() {
        if (Permissions.permissionsCamera(baseContext)) {
            startTakePhoto()
        } else {
            ActivityCompat.requestPermissions(
                this, Permissions.REQUIRED_PERMISSIONS_CAMERA, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun startTakePhoto() {
        takePhoto.launch(TakePhotoActivity.newIntent(this))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    startTakePhoto()
                } else {
                    Toast.makeText(
                        this,
                        getText(R.string.external_storage_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun uploadPhoto() {
        uploadPhoto.launch(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        )
    }

    private fun getBaby() = Gson().fromJson(intent.getStringExtra(BABY), Baby::class.java)

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 752
        private const val BABY = "baby"

        fun newIntent(context: Context, baby: Baby): Intent {
            val intent = Intent(context, BirthdayActivity::class.java)
            intent.putExtra(BABY, Gson().toJson(baby))
            return intent
        }
    }
}