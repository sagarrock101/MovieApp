package com.example.tmdb.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.tmdb.api.ApiFactory
import com.example.tmdb.model.NetworkStatus
import com.example.tmdb.model.MovieResponse
import com.example.tmdb.model.MovieResults
import com.example.tmdb.model.PopularMovieSearch
import com.example.tmdb.repository.MovieRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class MoviesViewModel() : ViewModel() {
    val TAG = "ViewModel"
    private val parentJob = Job()
    private val coroutineContext : CoroutineContext
    get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository: MovieRepository = MovieRepository(ApiFactory.MOVIE_SERVICE)

    private var popularMoviesMutableLiveData = MutableLiveData<PopularMovieSearch>()

    var movies : MediatorLiveData<PagedList<MovieResults>> = MediatorLiveData()
    var moviesLiveData: LiveData<MovieResponse>

    init {
        this.moviesLiveData = Transformations.switchMap(popularMoviesMutableLiveData) { search ->
            repository.getPopularMovies(search)
        }
    }

    fun fetchMovies(page: Int, movieType: String){
        val movieSearch = PopularMovieSearch(page, movieType)
        movies.addSource(repository.getPopular(movieSearch), movies::setValue)
        popularMoviesMutableLiveData.postValue(movieSearch)
    }

    fun cancelRequests() = coroutineContext.cancel()


    fun getMovie() : LiveData<PagedList<MovieResults>> {
        return movies
    }

    fun getMoviesStatus(): LiveData<NetworkStatus> {
        return repository.getPopularMoviesStatus()
    }




}