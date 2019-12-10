package com.example.tmdb.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.tmdb.api.ApiFactory
import com.example.tmdb.model.PopularMovieResponse
import com.example.tmdb.model.PopularMovieResults
import com.example.tmdb.model.PopularMovieSearch
import com.example.tmdb.repository.MovieRepository
import kotlinx.coroutines.*
import javax.security.auth.login.LoginException
import kotlin.coroutines.CoroutineContext


class PopularMoviesViewModel : ViewModel(){
    val TAG = "ViewModel"
    private val parentJob = Job()
    private val coroutineContext : CoroutineContext
    get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository: MovieRepository = MovieRepository(ApiFactory.MOVIE_SERVICE)

    private var popularMoviesMutableLiveData = MutableLiveData<PopularMovieSearch>()

    var popularMoviesLiveData: LiveData<PopularMovieResponse>

    init {
        this.popularMoviesLiveData = Transformations.switchMap(popularMoviesMutableLiveData) {search ->
            repository.getPopularMovies(search)
        }
    }

    fun fetchMovies(page: Int){
        val movieSearch = PopularMovieSearch(page)
        popularMoviesMutableLiveData.postValue(movieSearch)
    }

    fun cancelRequests() = coroutineContext.cancel()





}