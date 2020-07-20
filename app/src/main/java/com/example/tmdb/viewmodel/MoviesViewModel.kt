package com.example.tmdb.viewmodel

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.tmdb.model.*
import com.example.tmdb.paging.MovieDataSource
import com.example.tmdb.repository.MovieRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {
    val TAG = "ViewModel"

    @Inject
    lateinit var repository: MovieRepository

    var listState: Parcelable? = null

    private var trailersMutableLiveDataLiveData = MutableLiveData<TrailerSearch>()
    var trailersLiveData: LiveData<MovieTrailerResponse>

    private var reviewsMLD = MutableLiveData<Int>()
    var reviewsLD: LiveData<ReviewListResponse>

    var movies: MediatorLiveData<PagedList<Movie>> = MediatorLiveData()

    init {
        this.trailersLiveData =
            Transformations.switchMap(trailersMutableLiveDataLiveData) { search ->
                repository.getTrailersList(search)
            }

        this.reviewsLD = Transformations.switchMap(reviewsMLD) { movieId ->
            repository.getReviews(movieId)
        }
    }

    fun fetchReviews(movieId: Int) {
        reviewsMLD.postValue(movieId)
    }

    fun fetchMovies(page: Int, movieType: String) {
        val movieSearch = MovieSearch(page, movieType)
        movies.addSource(repository.getMovies(movieSearch), movies::setValue)
    }

    fun getNetworkStatus(): LiveData<NetworkState>? {
        return repository.networkStatusLiveData
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
        viewModelScope.launch {
            repository.insertMovieToDb(movie)
        }
    }

    fun deleteFromDb(id: Int) {
        viewModelScope.launch {
            repository.deleteMovieInDb(id)
        }
    }

    fun retry() {
        MovieDataSource.retryFailed()
    }

    fun getFavorites(): LiveData<List<Movie>> = runBlocking {
        repository.getFavoriteMovies()
    }

}