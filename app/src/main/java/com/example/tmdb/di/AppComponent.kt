package com.example.tmdb.di

import android.app.Application
import com.example.tmdb.ui.fragments.MovieFragment
import dagger.BindsInstance
import dagger.Component

@Component
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    fun inject(fragment: MovieFragment)
}