package com.example.tmdb.di

import android.app.Application
import com.example.tmdb.di.modules.AppModule
import com.example.tmdb.ui.activity.MainActivity
import com.example.tmdb.ui.fragments.MovieDetailFragment
import com.example.tmdb.ui.fragments.MovieFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    fun inject(fragment: MovieFragment)
    fun inject(fragment: MovieDetailFragment)
    fun inject(activity: MainActivity)
}