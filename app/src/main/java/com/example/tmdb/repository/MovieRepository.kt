package com.example.tmdb.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.tmdb.Paging.PopularMoviesDataSourceFactory
import com.example.tmdb.api.ApiFactory
import com.example.tmdb.api.TmdbService
import com.example.tmdb.model.PopularMovieResponse
import com.example.tmdb.model.PopularMovieResults
import com.example.tmdb.model.PopularMovieSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import javax.security.auth.login.LoginException

class MovieRepository(private val service : TmdbService)  {
    val popularMovieLiveData : MutableLiveData<PopularMovieResponse> =  MutableLiveData()
    val TAG = "MovieRepository"
    var config = PagedList.Config.Builder()
        .setPageSize(20)
        .setEnablePlaceholders(false)
        .build()

    lateinit var popularMoviesDataSourceFactory: PopularMoviesDataSourceFactory

    fun getPopularMovies(movieSearch: PopularMovieSearch) : MutableLiveData<PopularMovieResponse> {
        service.getPopularMovies(movieSearch.page)
            .enqueue(object : Callback<PopularMovieResponse>{
                override fun onFailure(call: Call<PopularMovieResponse>, t: Throwable) {
                    Log.e(TAG, "Error: " + t.message)
                }

                override fun onResponse(
                    call: Call<PopularMovieResponse>,
                    response: Response<PopularMovieResponse>
                ) {
                    if(response.isSuccessful) {
                        popularMovieLiveData.postValue(response.body())
                        Log.e(TAG, "OnSuccess: " + popularMovieLiveData.value)
                    }
                }


            })
        return popularMovieLiveData
    }

    fun getPopular(): LiveData<PagedList<PopularMovieResults>> {
        popularMoviesDataSourceFactory = PopularMoviesDataSourceFactory(ApiFactory.MOVIE_SERVICE)
        return LivePagedListBuilder<Int, PopularMovieResults>(popularMoviesDataSourceFactory, config).build()
    }

}