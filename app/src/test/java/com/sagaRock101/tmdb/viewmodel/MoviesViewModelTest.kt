package com.sagaRock101.tmdb.viewmodel

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MoviesViewModelTest {

    val viewModel = MoviesViewModel(ApplicationProvider.getApplicationContext())

    fun fetch_movie_event() {
        viewModel.fetchMovies(1, "popular")
    }

}