package com.example.tmdb.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.tmdb.api.TmdbService
import com.example.tmdb.database.MovieDao
import com.example.tmdb.database.MovieDatabase
import com.example.tmdb.model.*
import com.example.tmdb.paging.PopularMoviesDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MovieRepository @Inject constructor( val service : TmdbService,  var context: Context)  {

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

     var networkStatusLiveData: LiveData<NetworkState>? =null

    fun getPopular(search: MovieSearch): LiveData<PagedList<Movie>> {
        popularMoviesDataSourceFactory = PopularMoviesDataSourceFactory(service, search)
        networkStatusLiveData = Transformations
            .switchMap(
                popularMoviesDataSourceFactory.moviesDataSource) {
            it.networkState
            }
        return LivePagedListBuilder<Int, Movie>(popularMoviesDataSourceFactory, config).build()
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