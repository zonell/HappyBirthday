package com.nanit.happybirthday.feature.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nanit.happybirthday.R
import com.nanit.happybirthday.feature.b_day.BirthdayActivity
import com.nanit.happybirthday.feature.main.model.PhotoType
import com.nanit.happybirthday.feature.takePhoto.TakePhotoActivity
import com.nanit.happybirthday.feature.takePhoto.TakePhotoActivity.Companion.PHOTO
import com.nanit.happybirthday.feature.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.photo_item.*
import kotlinx.android.synthetic.main.take_photo_dialog.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MainActivity : AppCompatActivity() {

    private val mainVM by viewModel<MainViewModel>()
    private var dialog: DatePickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
    }

    private fun initUI() {
        initDate()
        initViews()
        initDatePicker()
        setStatusBarColor(R.color.lightBlueGreen)
    }

    private fun initViews() {
        etName.addTextChangedListener(object : TextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                mainVM.nameEntered(s.toString())
            }
        })

        actionBirthday.setOnClickListener {
            it.hideKeyboard()
            dialog?.show()
        }
        content.setOnClickListener { it.hideKeyboard() }
        ivCamera.setOnClickListener {
            it.hideKeyboard()
            showPhotoDialog()
        }
        btnNext.setOnClickListener {
            it.hideKeyboard()
            startActivity(BirthdayActivity.newIntent(this, mainVM.getBaby()))
        }
    }

    private fun initDate() {
        mainVM.buttonState.observe(this, this::buttonState)
    }

    private fun buttonState(isEnable: Boolean) {
        btnNext.enableIf { isEnable }
    }

    @SuppressLint("SetTextI18n")
    private fun initDatePicker() {
        val calendar: Calendar = Calendar.getInstance()

        val date =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                actionBirthday.text = "$day - ${month + 1} - $year"
                mainVM.bDayEntered(calendar)
            }

        dialog = DatePickerDialog(
            this,
            date,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        val min = Calendar.getInstance()
        min.set(
            calendar.get(Calendar.YEAR) - MAX_AGE,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        dialog?.datePicker?.apply {
            maxDate = System.currentTimeMillis()
            minDate = min.timeInMillis
        }
    }

    private var takePhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    it.getStringExtra(PHOTO)?.replace(FILE_PATH, EMPTY_STRING).apply {
                        mainVM.photoAdded(this, PhotoType.TAKE)
                        ivPhoto.apply {
                            setImageBitmap(getImgBitmap())
                            post { changeRadius(ivCamera) }
                        }
                    }
                }
            }
        }

    private var uploadPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    intent.data?.let {
                        mainVM.photoAdded(it.toString(), PhotoType.UPLOAD)
                        ivPhoto.apply {
                            setImageURI(it)
                            post { changeRadius(ivCamera) }
                        }
                    }
                }
            }
        }

    private fun showPhotoDialog() {
        DialogBuilder(this).build().apply {
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

    private fun takePhoto() {
        if (allPermissionsGranted()) {
            startTakePhoto()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun uploadPhoto() {
        uploadPhoto.launch(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        )
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val FILE_PATH = "file:///"
        private const val REQUEST_CODE_PERMISSIONS = 752
        private const val MAX_AGE = 12

        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}