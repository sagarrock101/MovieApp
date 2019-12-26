package com.example.tmdb.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.tmdb.Paging.MovieDataSource
import com.example.tmdb.Paging.PopularMoviesDataSourceFactory
import com.example.tmdb.api.ApiFactory
import com.example.tmdb.api.TmdbService
import com.example.tmdb.model.NetworkStatus
import com.example.tmdb.model.MovieResponse
import com.example.tmdb.model.MovieResults
import com.example.tmdb.model.MovieSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository(private val service : TmdbService)  {
    val movieLiveData : MutableLiveData<MovieResponse> =  MutableLiveData()
    val TAG = "MovieRepository"
    var config = PagedList.Config.Builder()
        .setPageSize(20)
        .setEnablePlaceholders(false)
        .build()

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

    fun getPopular(search: MovieSearch): LiveData<PagedList<MovieResults>> {
        popularMoviesDataSourceFactory = PopularMoviesDataSourceFactory(ApiFactory.MOVIE_SERVICE, search)
        popularMoviesStatus = Transformations
            .switchMap(
                popularMoviesDataSourceFactory.getPopularMoviesDataSource(),
                MovieDataSource::getNetworkState
            )
        return LivePagedListBuilder<Int, MovieResults>(popularMoviesDataSourceFactory, config).build()
    }

    fun getPopularMoviesStatus(): LiveData<NetworkStatus> {
        return popularMoviesStatus
    }




}