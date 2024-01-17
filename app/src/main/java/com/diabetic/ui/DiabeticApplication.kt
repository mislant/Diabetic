package com.diabetic.ui

import android.app.Application

class DiabeticApplication : Application() {
    override fun onCreate() {
        ServiceLocator.init(applicationContext)
        super.onCreate()
    }
}