package com.nanit.happybirthday.feature.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import com.nanit.happybirthday.R
import com.nanit.happybirthday.feature.utils.TextWatcher
import com.nanit.happybirthday.feature.utils.enableIf
import com.nanit.happybirthday.feature.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
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
    }

    private fun initViews() {
        etName.addTextChangedListener(object : TextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                mainVM.nameEntered(s.toString())
            }
        })

        actionBirthday.setOnClickListener { dialog?.show() }
        content.setOnClickListener { it.hideKeyboard() }
    }

    private fun initDate() {
        mainVM.buttonState.observe(this, this::buttonState)
    }

    private fun buttonState(isEnable: Boolean) {
        btnNext.enableIf { isEnable }
    }

    private fun initDatePicker() {
        val calendar: Calendar = Calendar.getInstance()

        val date =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                val date = "$day ${month + 1} $year"

                actionBirthday.text = date
                mainVM.bDayEntered(date)
            }

        dialog = DatePickerDialog(
            this,
            date,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        dialog?.datePicker?.apply { maxDate = System.currentTimeMillis() }
    }
}