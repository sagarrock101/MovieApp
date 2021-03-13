package com.sagaRock101.tmdb.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sagaRock101.tmdb.api.TmdbService
import com.sagaRock101.tmdb.database.MovieDatabase
import com.sagaRock101.tmdb.model.*
import com.sagaRock101.tmdb.paging.PopularMoviesDataSourceFactory
import com.sagaRock101.tmdb.paging.SearchDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MovieRepository @Inject constructor(val service: TmdbService, var context: Context) {

    val TAG = "MovieRepository"
    var config = PagedList.Config.Builder()
        .setPageSize(5)
        .setEnablePlaceholders(false)
        .build()

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var movie = MutableLiveData<Movie?>()

    var database = MovieDatabase.getInstance(context)
    lateinit var popularMoviesDataSourceFactory: PopularMoviesDataSourceFactory

    lateinit var searchMovieDataSourceFactory: SearchDataSourceFactory

    var networkStatusLiveData: LiveData<NetworkState>? = null

    var searchNetworkStatusLiveData: LiveData<NetworkState>? = null

    fun getMovies(search: MovieSearch): LiveData<PagedList<Movie>> {
        popularMoviesDataSourceFactory = PopularMoviesDataSourceFactory(
            service,
            search
        )
        networkStatusLiveData = Transformations
            .switchMap(popularMoviesDataSourceFactory.moviesDataSource) {
                it.networkState
            }
        return LivePagedListBuilder<Int, Movie>(popularMoviesDataSourceFactory, config).build()

    }

    fun searchMovie(search: String): LiveData<PagedList<Movie>> {
        searchMovieDataSourceFactory = SearchDataSourceFactory(
            service,
            search
        )
        searchNetworkStatusLiveData =
            Transformations.switchMap(searchMovieDataSourceFactory.moviesDataSource) {
                it.networkState
            }

        return LivePagedListBuilder<Int, Movie>(searchMovieDataSourceFactory, config).build()
    }

//    fun searchSuggestions(search: String) = flow{
//        emit(Result.loading(null))
//        try {
//            val result = service.searchSuggestion(search)
//            emit(Result.success(result))
//        } catch (e: Exception) {
//            emit(Result.error(e.message!!))
//        }
//    }

    fun getTrailersList(trailerSearch: TrailerSearch): MutableLiveData<MovieTrailerResponse> {
        var liveData: MutableLiveData<MovieTrailerResponse> = MutableLiveData()
        service.getTrailerList(trailerSearch.movieId!!)
            .enqueue(object : Callback<MovieTrailerResponse> {
                override fun onFailure(call: Call<MovieTrailerResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<MovieTrailerResponse>,
                    response: Response<MovieTrailerResponse>
                ) {
                    liveData.postValue(response.body())
                }

            })

        return liveData

    }

    fun getReviews(movieId: Int): MutableLiveData<ReviewListResponse> {
        var liveData: MutableLiveData<ReviewListResponse> = MutableLiveData()
        service.getReviews(movieId)
            .enqueue(object : Callback<ReviewListResponse> {
                override fun onFailure(call: Call<ReviewListResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<ReviewListResponse>,
                    response: Response<ReviewListResponse>
                ) {
                    liveData.postValue(response.body())
                }

            })

        return liveData

    }

    fun searchSuggestion(query: String): MutableLiveData<MovieResponse> {
        var liveData: MutableLiveData<MovieResponse> = MutableLiveData()
        service.searchSuggestion(query).enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                liveData.postValue(response.body())
            }
        })

        return liveData
    }

    fun getTags(movieId: Int): MutableLiveData<MovieKeywords> {
        var liveData: MutableLiveData<MovieKeywords> = MutableLiveData()
        service.getKeywords(movieId).enqueue(object : Callback<MovieKeywords> {
            override fun onFailure(call: Call<MovieKeywords>, t: Throwable) {
            }

            override fun onResponse(call: Call<MovieKeywords>, response: Response<MovieKeywords>) {
                liveData.postValue(response.body())
            }

        })
        return liveData
    }

    private val _movie = MutableLiveData<Movie>()
    lateinit var currentMovie: LiveData<Movie>

    fun getMovieFromDb(id: Int) {
        currentMovie = (database.movieDao.getMovie(id))
    }

    suspend fun insertMovieToDb(movie: Movie) {
        uiScope.launch {
            database.movieDao.insert(movie)
        }
    }

    suspend fun deleteMovieInDb(id: Int) {
        uiScope.launch {
            database.movieDao.deleteMovie(id)
        }
    }

    fun getFavoriteMovies(): LiveData<List<Movie>> {
        return database.movieDao.getMovieList()
    }

}