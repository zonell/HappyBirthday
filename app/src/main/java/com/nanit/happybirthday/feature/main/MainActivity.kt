package com.nanit.happybirthday.feature.main

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
import com.nanit.happybirthday.R
import com.nanit.happybirthday.feature.b_day.BirthdayActivity
import com.nanit.happybirthday.feature.main.model.PhotoType
import com.nanit.happybirthday.feature.takePhoto.TakePhotoActivity
import com.nanit.happybirthday.feature.takePhoto.TakePhotoActivity.Companion.PHOTO
import com.nanit.happybirthday.feature.utils.*
import com.nanit.happybirthday.feature.utils.Permissions.Companion.REQUIRED_PERMISSIONS_CAMERA
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.photo_item.*
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
            DialogBuilder(this).showPhotoDialog({ takePhoto() }, { uploadPhoto() })
        }
        btnNext.setOnClickListener {
            it.hideKeyboard()
            startActivity(BirthdayActivity.newIntent(this, mainVM.getBaby()))
        }

        ivPhoto.setPhoto(R.color.paleTeal, R.drawable.ic_placeholder_green)

        ivPhoto.viewTreeObserver.addOnDrawListener {
            ivPhoto.post { ivPhoto.changeRadius(ivCamera, windowManager) }
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
                    it.getStringExtra(PHOTO)?.apply {
                        mainVM.photoAdded(this, PhotoType.TAKE)
                        ivPhoto.setPhoto(R.color.paleTeal, this)
                    }
                }
            }
        }

    private var uploadPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let {
                    mainVM.photoAdded(it.toString(), PhotoType.UPLOAD)
                    ivPhoto.setPhoto(R.color.paleTeal, it)
                }
            }
        }

    private fun takePhoto() {
        if (Permissions.permissionsCamera(baseContext)) {
            startTakePhoto()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS_CAMERA, REQUEST_CODE_PERMISSIONS
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

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 752
        private const val MAX_AGE = 12
    }
}