package com.example.jetpackcompose

import android.app.Application
import com.example.jetpackcompose.di.component.AppComponent
import com.example.jetpackcompose.di.component.DaggerAppComponent


class MVVMApplication : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }
}