package com.example.tmdb.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.tmdb.api.ApiFactory
import com.example.tmdb.model.*
import com.example.tmdb.repository.MovieRepository


class MoviesViewModel(application: Application) : AndroidViewModel(application) {
    val TAG = "ViewModel"

    var favoritesSelected: MutableLiveData<Boolean> = MutableLiveData()

    private val repository: MovieRepository = MovieRepository(ApiFactory.MOVIE_SERVICE, application)

    private var moviesMutableLiveData = MutableLiveData<MovieSearch>()
     var popularMoviesLiveData : LiveData<PagedList<Movie>>

    private  var trailersMutableLiveDataLiveData = MutableLiveData<TrailerSearch>()
     var trailersLiveData: LiveData<MovieTrailerResponse>
//    var moviesLiveData: LiveData<MovieResponse>

    var movies : MediatorLiveData<PagedList<Movie>> = MediatorLiveData()

    init {
        this.popularMoviesLiveData = Transformations.switchMap(moviesMutableLiveData) { search ->
            if(search.movieType == "favorites") {
                repository.getFavoriteMovies(search)
            } else {
                repository.getPopular(search)
            }
        }

        this.trailersLiveData = Transformations.switchMap(trailersMutableLiveDataLiveData) {search ->
            repository.getTrailersList(search)
        }





    }
    
    fun fetchMovies(page: Int, movieType: String){


            val movieSearch = MovieSearch(page, movieType)
            moviesMutableLiveData.postValue(movieSearch)

    }
    fun fetchMovies(movieType: String) {
        val movieSearch = MovieSearch(1, movieType)
        moviesMutableLiveData.postValue(movieSearch)

    }

    fun getMovie() : LiveData<PagedList<Movie>> {
        return movies
    }

    fun getMoviesStatus(): LiveData<NetworkStatus> {
        return repository.getPopularMoviesStatus()
    }

    fun getTrailers(id: Int) {
        var trailerSearch = TrailerSearch(id)
        trailersMutableLiveDataLiveData.value = trailerSearch
    }

    fun getMovieFromDb(): LiveData<Movie> {
        return repository.currentMovie
    }

    fun getMovieCheck(id: Int) {
        repository.getMovieFromDb(id)
    }

    fun insertToDb(movie: Movie) {
        repository.insertMovieToDb(movie)
    }

    fun deleteFromDb(id: Int) {
        repository.deleteMovieInDb(id)
    }


}