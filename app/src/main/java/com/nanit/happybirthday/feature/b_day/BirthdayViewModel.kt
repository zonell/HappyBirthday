package com.nanit.happybirthday.feature.b_day

import androidx.lifecycle.ViewModel
import com.nanit.happybirthday.feature.b_day.model.UI

class BirthdayViewModel : ViewModel() {

    fun getUI(): UI = UI.getUI()
}