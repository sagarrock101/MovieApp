package com.sagaRock101.tmdb

import android.app.Application
import com.sagaRock101.tmdb.di.AppComponent
import com.sagaRock101.tmdb.di.DaggerAppComponent
import com.facebook.stetho.Stetho


class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }


    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

}