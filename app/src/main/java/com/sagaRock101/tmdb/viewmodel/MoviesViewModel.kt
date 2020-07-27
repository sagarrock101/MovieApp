package com.sagaRock101.tmdb.viewmodel

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.sagaRock101.tmdb.databinding.LayoutSearchBinding
import com.sagaRock101.tmdb.model.*
import com.sagaRock101.tmdb.paging.MovieDataSource
import com.sagaRock101.tmdb.paging.SearchDataSource
import com.sagaRock101.tmdb.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton
import com.sagaRock101.tmdb.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map

@Singleton
class MoviesViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {
    val TAG = "ViewModel"

    sealed class SearchResult {
        class ValidResult(val matches: MovieResponse) : SearchResult()
        class Error(e: Exception) : SearchResult()
    }

    @Inject
    lateinit var repository: MovieRepository

    var listState: Parcelable? = null

    var searchListState: Parcelable? = null

    var searchViewBinding: LayoutSearchBinding? = null

    var rvSearchVisibility: Int? = null

    private var trailersMutableLiveDataLiveData = MutableLiveData<TrailerSearch>()
    var trailersLiveData: LiveData<MovieTrailerResponse>

    private var reviewsMLD = MutableLiveData<Int>()
    var reviewsLD: LiveData<ReviewListResponse>

    var movies: MediatorLiveData<PagedList<Movie>> = MediatorLiveData()

    var searchMovies: MediatorLiveData<PagedList<Movie>> = MediatorLiveData()

    private var searchSuggestionMLD = MutableLiveData<String>()

//    @ExperimentalCoroutinesApi
//    var queryChannel = BroadcastChannel<String>(Channel.CONFLATED)
//
//    @ExperimentalCoroutinesApi
//    val searchResult = queryChannel
//        .asFlow()
//        .debounce(500)
//        .map {
//            try {
//            } catch (e: Throwable) {
//                if (e is CancellationException) {
//                    throw e
//                } else {
////                    SearchResult.Error(e)
//                }
//            }
//        }

     val searchSuggestionLD: LiveData<MovieResponse>


    init {
        this.trailersLiveData =
            Transformations.switchMap(trailersMutableLiveDataLiveData) { search ->
                repository.getTrailersList(search)
            }

        this.reviewsLD = Transformations.switchMap(reviewsMLD) { movieId ->
            repository.getReviews(movieId)
        }

        this.searchSuggestionLD = Transformations.switchMap(searchSuggestionMLD) {
            repository.searchSuggestion(it)
        }
    }

    fun setSearchBinding(binding: LayoutSearchBinding) {
        searchViewBinding = binding
    }



    fun getSearchBinding() = searchViewBinding

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

    fun getSearchNetworkStatus(): LiveData<NetworkState>? {
        return repository.searchNetworkStatusLiveData
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

    fun retrySearch() {
        SearchDataSource.retryFailed()
    }

    fun getFavorites(): LiveData<List<Movie>> = runBlocking {
        repository.getFavoriteMovies()
    }

    fun searchMovie(query: String?) {
        searchMovies.addSource(repository.searchMovie(query!!), searchMovies::setValue )
    }

    fun searchSuggestion(query: String) {
        searchSuggestionMLD.postValue(query)
    }


}