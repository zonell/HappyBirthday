package com.nanit.happybirthday.feature.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nanit.happybirthday.feature.main.model.Baby
import com.nanit.happybirthday.feature.main.model.DataType
import java.util.*

class MainViewModel : ViewModel() {

    private val calendar: Calendar = Calendar.getInstance()

    companion object {
        private const val MIN_NAME_LENGTH = 2
    }

    private var baby = Baby()

    val buttonState = MutableLiveData(false)

    fun nameEntered(name: String) {
        baby.name = name
        updateBtnState()
    }

    fun bDayEntered(calendar: Calendar) {
        val year = this.calendar.get(Calendar.YEAR) - calendar.get(Calendar.YEAR)
        if (year > 0) {
            baby.apply {
                bDay = year.toString()
                dataType = DataType.YEAR
            }
        } else {
            baby.apply {
                val month = this@MainViewModel.calendar.get(Calendar.MONTH) - calendar.get(Calendar.MONTH)
                bDay = (if (month == 0) month + 1 else month).toString()
                dataType = DataType.MONTH
            }

        }
        updateBtnState()
    }

    fun photoAdded(photoUri: String?) {
        baby.photoUri = photoUri ?: ""
    }

    private fun updateBtnState() {
        buttonState.value =
            baby.name.isNotEmpty() &&
                    baby.name.length >= MIN_NAME_LENGTH &&
                    baby.bDay.isNotEmpty()
    }

    fun getBaby() = baby
}