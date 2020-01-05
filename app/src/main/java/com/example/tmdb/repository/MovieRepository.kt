package com.example.tmdb.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.tmdb.Paging.MovieDataSource
import com.example.tmdb.Paging.PopularMoviesDataSourceFactory
import com.example.tmdb.api.ApiFactory
import com.example.tmdb.api.TmdbService
import com.example.tmdb.database.MovieDao
import com.example.tmdb.database.MovieDatabase
import com.example.tmdb.model.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MovieRepository @Inject constructor( val service : TmdbService,  var context: Context)  {
    val movieLiveData : MutableLiveData<MovieResponse> =  MutableLiveData()
    lateinit var movieDao: MovieDao
    val TAG = "MovieRepository"
    var config = PagedList.Config.Builder()
        .setPageSize(20)
        .setEnablePlaceholders(false)
        .build()

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var movie = MutableLiveData<Movie?>()

    var database = MovieDatabase.getInstance(context)
    lateinit var popularMoviesDataSourceFactory: PopularMoviesDataSourceFactory

    private var popularMoviesStatus: LiveData<NetworkStatus> = MutableLiveData<NetworkStatus>()

    fun getPopularMovies(movieSearch: MovieSearch) : MutableLiveData<MovieResponse> {
        service.getMovies(movieSearch.movieType, movieSearch.page)
            .enqueue(object : Callback<MovieResponse>{
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e(TAG, "Error: " + t.message)
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if(response.isSuccessful) {
                        movieLiveData.postValue(response.body())
                        Log.e(TAG, "OnSuccess: " + movieLiveData.value)
                    }
                }


            })
        return movieLiveData
    }

    fun getPopular(search: MovieSearch): LiveData<PagedList<Movie>> {
        popularMoviesDataSourceFactory = PopularMoviesDataSourceFactory(ApiFactory.MOVIE_SERVICE, search)
        popularMoviesStatus = Transformations
            .switchMap(
                popularMoviesDataSourceFactory.getPopularMoviesDataSource(),
                MovieDataSource::getNetworkState
            )
        return LivePagedListBuilder<Int, Movie>(popularMoviesDataSourceFactory, config).build()
    }

    fun getPopularMoviesStatus(): LiveData<NetworkStatus> {
        return popularMoviesStatus
    }

    fun getTrailersList(trailerSearch: TrailerSearch) : MutableLiveData<MovieTrailerResponse> {
        var liveData: MutableLiveData<MovieTrailerResponse> = MutableLiveData()
        service.getTrailerList(trailerSearch.movieId!!)
            .enqueue(object : Callback<MovieTrailerResponse>{
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

    private val _movie = MutableLiveData<Movie>()
     lateinit var currentMovie: LiveData<Movie>

    fun getMovieFromDb(id: Int) {
        currentMovie = (database.movieDao.getMovie(id))
    }

    fun insertMovieToDb(movie: Movie) {
        Toast.makeText(context, "Inserted to DB",Toast.LENGTH_SHORT).show()
        uiScope.launch {
            database.movieDao.insert(movie)
        }
    }

    fun deleteMovieInDb(id: Int) {
        Toast.makeText(context, "Deleted from DB",Toast.LENGTH_SHORT).show()
        uiScope.launch {
            database.movieDao.deleteMovie(id)
        }
    }

    fun getFavoriteMovies(movieSearch: MovieSearch) : LiveData<PagedList<Movie>>  {
        return LivePagedListBuilder<Int, Movie>(database.movieDao.getMovieList(), config).build()
    }

}