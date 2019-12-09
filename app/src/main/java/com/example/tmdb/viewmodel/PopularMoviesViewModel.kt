package com.example.tmdb.viewmodel

import android.R
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tmdb.api.ApiFactory
import com.example.tmdb.model.PopularMovieResults
import com.example.tmdb.repository.MovieRepository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class PopularMoviesViewModel : ViewModel(){

    private val parentJob = Job()
    private val coroutineContext : CoroutineContext
    get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository: MovieRepository = MovieRepository(ApiFactory.MovieApi)

    val popularMoviesLiveData = MutableLiveData<List<PopularMovieResults>>()

    fun fetchMovies() {
        scope.launch {
            val popularMovies = repository.getPopularMovies()
            popularMoviesLiveData.postValue(popularMovies)
        }
    }

    fun cancelRequests() = coroutineContext.cancel()





}