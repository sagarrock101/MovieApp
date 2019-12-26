package com.example.tmdb.Paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.tmdb.api.TmdbService
import com.example.tmdb.model.NetworkStatus
import com.example.tmdb.model.MovieResponse
import com.example.tmdb.model.MovieResults
import com.example.tmdb.model.MovieSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDataSource(private val apiService: TmdbService, private val search: MovieSearch)
    : PageKeyedDataSource<Int, MovieResults>() {

    private val networkState: MutableLiveData<NetworkStatus> = MutableLiveData()
    val TAG = "MovieDataSource"
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MovieResults>
    ) {
        apiService.getMovies(search.movieType, FIRST_PAGE)
            .enqueue(object : Callback<MovieResponse>{
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if(!response.body()?.results?.isEmpty()!!) {
                        networkState.postValue(NetworkStatus.SUCCESS)
                        callback.onResult(response.body()?.results!!, null,
                            FIRST_PAGE + 1)
                    } else {
                        networkState.postValue(NetworkStatus.EMPTY)
                    }
                }

            })
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MovieResults>
    ) {
        apiService.getMovies(search.movieType, params.key)
            .enqueue(object : Callback<MovieResponse>{
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if(!response.body()?.results?.isEmpty()!!) run {
                        networkState.postValue(NetworkStatus.SUCCESS)
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
        callback: LoadCallback<Int, MovieResults>
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
        const val PAGE_SIZE = 50
        const val FIRST_PAGE = 1

    }

    fun getNetworkState(): MutableLiveData<NetworkStatus> {
        return networkState
    }
}