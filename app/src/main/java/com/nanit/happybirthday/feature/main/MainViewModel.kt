package com.nanit.happybirthday.feature.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private var name = ""
    private var bDay = ""

    val buttonState = MutableLiveData(false)

    fun nameEntered(name: String) {
        this.name = name
        updateBtnState()
    }

    fun bDayEntered(bDay: String) {
        this.bDay = bDay
        updateBtnState()
    }

    private fun updateBtnState() {
        buttonState.value = name.isNotEmpty() && bDay.isNotEmpty()
    }
}