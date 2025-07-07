package com.example.what3words

import android.app.Application
import core.di.initKoin
import org.koin.android.ext.koin.androidContext

@Suppress("unused")
class MainApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin{
            androidContext(this@MainApp)
        }
    }
}