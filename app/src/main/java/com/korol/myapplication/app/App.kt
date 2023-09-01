package com.korol.myapplication.app

import android.app.Application
import com.korol.myapplication.di.AppComponent
import com.korol.myapplication.di.DaggerAppComponent

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(context = applicationContext)
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}
