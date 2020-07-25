package com.sagaRock101.tmdb.di

import android.app.Application
import com.sagaRock101.tmdb.di.modules.AppModule
import com.sagaRock101.tmdb.ui.activity.MainActivity
import com.sagaRock101.tmdb.ui.fragments.MovieDetailFragment
import com.sagaRock101.tmdb.ui.fragments.MovieFavorite
import com.sagaRock101.tmdb.ui.fragments.MovieFragment
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
    fun inject(fragment: MovieFavorite)
}