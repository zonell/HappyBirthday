package com.nanit.happybirthday.di.module

import com.nanit.happybirthday.feature.b_day.BirthdayViewModel
import com.nanit.happybirthday.feature.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModelModule {

    companion object {
        val viewModelModule = module {
            viewModel<MainViewModel>()
            viewModel<BirthdayViewModel>()
        }
    }
}