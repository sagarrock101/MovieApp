package com.example.tmdb.Paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.tmdb.api.TmdbService
import com.example.tmdb.model.NetworkStatus
import com.example.tmdb.model.PopularMovieResponse
import com.example.tmdb.model.PopularMovieResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PopularMovieDataSource(private val apiService: TmdbService)
    : PageKeyedDataSource<Int, PopularMovieResults>() {

    private val networkState: MutableLiveData<NetworkStatus> = MutableLiveData<NetworkStatus>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PopularMovieResults>
    ) {
        apiService.getPopularMovies(FIRST_PAGE)
            .enqueue(object : Callback<PopularMovieResponse>{
                override fun onFailure(call: Call<PopularMovieResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<PopularMovieResponse>,
                    response: Response<PopularMovieResponse>
                ) {
                    if(!response.body()?.results?.isEmpty()!!) {
                        networkState.postValue(NetworkStatus.SUCCESS)
                        callback.onResult(response.body()?.results!!, null, FIRST_PAGE + 1)
                    } else {
                        networkState.postValue(NetworkStatus.EMPTY)
                    }
                }

            })
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PopularMovieResults>
    ) {
        apiService.getPopularMovies(params.key)
            .enqueue(object : Callback<PopularMovieResponse>{
                override fun onFailure(call: Call<PopularMovieResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<PopularMovieResponse>,
                    response: Response<PopularMovieResponse>
                ) {
                    if(response.isSuccessful) run {
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
        callback: LoadCallback<Int, PopularMovieResults>
    ) {
        apiService.getPopularMovies(params.key)
            .enqueue(object : Callback<PopularMovieResponse>{
                override fun onFailure(call: Call<PopularMovieResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<PopularMovieResponse>,
                    response: Response<PopularMovieResponse>
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
}