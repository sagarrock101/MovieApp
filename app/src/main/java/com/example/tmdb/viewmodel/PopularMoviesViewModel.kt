package com.example.tmdb.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.ItemKeyedDataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.tmdb.Paging.PopularMoviesDataSourceFactory
import com.example.tmdb.api.ApiFactory
import com.example.tmdb.model.PopularMovieResponse
import com.example.tmdb.model.PopularMovieResults
import com.example.tmdb.model.PopularMovieSearch
import com.example.tmdb.repository.MovieRepository
import kotlinx.coroutines.*
import javax.security.auth.login.LoginException
import kotlin.coroutines.CoroutineContext


class PopularMoviesViewModel() : ViewModel() {
    val TAG = "ViewModel"
    private val parentJob = Job()
    private val coroutineContext : CoroutineContext
    get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository: MovieRepository = MovieRepository(ApiFactory.MOVIE_SERVICE)

    private var popularMoviesMutableLiveData = MutableLiveData<PopularMovieSearch>()

    var popularMovies : MediatorLiveData<PagedList<PopularMovieResults>> = MediatorLiveData()
    var popularMoviesLiveData: LiveData<PopularMovieResponse>

    init {
        this.popularMoviesLiveData = Transformations.switchMap(popularMoviesMutableLiveData) {search ->
            repository.getPopularMovies(search)
        }
        popularMovies.addSource(repository.getPopular(), popularMovies::setValue)
    }

    fun fetchMovies(page: Int){
        val movieSearch = PopularMovieSearch(page)
        popularMoviesMutableLiveData.postValue(movieSearch)
    }

    fun cancelRequests() = coroutineContext.cancel()


    fun getMovie() : LiveData<PagedList<PopularMovieResults>> {
        return popularMovies
    }




}