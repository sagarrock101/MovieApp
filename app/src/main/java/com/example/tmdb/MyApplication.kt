package com.example.tmdb

import android.app.Application
import com.example.tmdb.di.AppComponent
import com.example.tmdb.di.DaggerAppComponent
import com.facebook.stetho.Stetho


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }
}