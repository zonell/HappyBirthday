package com.nanit.happybirthday

import android.app.Application
import com.nanit.happybirthday.di.module.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            module {
                androidContext(this@App)
                modules(
                    listOf(
                        ViewModelModule.viewModelModule,
                    )
                )
            }
        }
    }
}