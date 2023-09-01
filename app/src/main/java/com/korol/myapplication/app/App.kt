package com.korol.myapplication.app

import android.app.Application
import com.korol.myapplication.di.AppComponent
import com.korol.myapplication.di.AppModule
import com.korol.myapplication.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(context = this))
            .build()
    }
}
