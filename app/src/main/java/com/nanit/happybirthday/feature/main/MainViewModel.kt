package com.nanit.happybirthday.feature.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nanit.happybirthday.feature.main.model.Baby
import com.nanit.happybirthday.feature.main.model.DataType
import com.nanit.happybirthday.feature.main.model.PhotoType
import java.util.*

class MainViewModel : ViewModel() {

    private val calendar: Calendar = Calendar.getInstance()

    companion object {
        private const val MIN_NAME_LENGTH = 2
        private const val MONTH_IN_YEAR = 12
    }

    private var baby = Baby()

    val buttonState = MutableLiveData(false)

    fun nameEntered(name: String) {
        baby.name = name
        updateBtnState()
    }

    fun bDayEntered(calendar: Calendar) {
        val year = this.calendar.get(Calendar.YEAR) - calendar.get(Calendar.YEAR)
        val month = this@MainViewModel.calendar.get(Calendar.MONTH) - calendar.get(Calendar.MONTH)
        val day = this@MainViewModel.calendar.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH)
        when {
            year > 1 -> {
                baby.apply {
                    when {
                        month > 0 -> {
                            bDay = year.toString()
                            dataType = DataType.YEAR
                        }
                        month == 0 -> {
                            if (day >= 0) {
                                bDay = year.toString()
                                dataType = DataType.YEAR
                            } else {
                                bDay = (year - 1).toString()
                                dataType = DataType.YEAR
                            }
                        }
                        else -> {
                            bDay = (year - 1).toString()
                            dataType = DataType.YEAR
                        }
                    }
                }
            }
            year > 0 -> {
                baby.apply {
                    when {
                        month > 0 -> {
                            bDay = year.toString()
                            dataType = DataType.YEAR
                        }
                        month == 0 -> {
                            if (day >= 0) {
                                bDay = year.toString()
                                dataType = DataType.YEAR
                            } else {
                                bDay = (MONTH_IN_YEAR - 1).toString()
                                dataType = DataType.MONTH
                            }
                        }
                        else -> {
                            bDay = (if (day >= 0) (MONTH_IN_YEAR + month) else (MONTH_IN_YEAR + month - 1)).toString()
                            dataType = DataType.MONTH
                        }
                    }
                }
            }
            else -> {
                baby.apply {
                    when {
                        month > 0 -> {
                            bDay = (if (day >= 0) month else month - 1).toString()
                            dataType = DataType.MONTH
                        }
                        else -> {
                            bDay = month.toString()
                            dataType = DataType.MONTH
                        }
                    }
                }
            }
        }
        updateBtnState()
    }

    fun photoAdded(photoUri: String?, photoType: PhotoType) {
        baby.apply {
            this.photoUri = photoUri
            this.photoType = photoType
        }
    }

    private fun updateBtnState() {
        buttonState.value =
            baby.name.isNotEmpty() &&
                    baby.name.length >= MIN_NAME_LENGTH &&
                    baby.bDay.isNotEmpty()
    }

    fun getBaby() = baby
}