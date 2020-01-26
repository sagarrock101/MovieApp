package com.example.tmdb.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.tmdb.api.TmdbService
import com.example.tmdb.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDataSource(private val apiService: TmdbService, private val search: MovieSearch)
    : PageKeyedDataSource<Int, Movie>() {

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    val TAG = "MovieDataSource"
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)
        apiService.getMovies(search.movieType, FIRST_PAGE)
            .enqueue(object : Callback<MovieResponse>{
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if(response.isSuccessful) {
                        if(response.body()?.results?.isNotEmpty()!!) {
                            callback.onResult(response.body()?.results!!, null,
                                FIRST_PAGE + 1)
                        } else {
                        }
                    }
                }

            })
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)
        apiService.getMovies(search.movieType, params.key)
            .enqueue(object : Callback<MovieResponse>{
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if(!response.body()?.results?.isEmpty()!!) run {
                        networkState.postValue(NetworkState.LOADED)
                        var nextPageNumber = params.key + 1
                        var totalPages = response.body()!!.totalPages
                        var key = if(totalPages > nextPageNumber) nextPageNumber else null
                        callback.onResult(response.body()!!.results, key)
                    }
                }

            })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Movie>
    ) {
        apiService.getMovies(search.movieType, params.key)
            .enqueue(object : Callback<MovieResponse>{
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if(response.body()!!.results.isEmpty()) {
                        var key = if(params.key > 1) params.key -1 else null
                        callback.onResult(response.body()!!.results, key)
                    }
                }

            })
    }

    companion object {
        const val FIRST_PAGE = 1
    }

}