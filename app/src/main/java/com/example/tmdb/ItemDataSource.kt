package com.example.tmdb

import androidx.paging.PageKeyedDataSource
import com.example.tmdb.api.ApiFactory
import com.example.tmdb.model.PopularMovieResponse

import com.example.tmdb.model.PopularMovieResults
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ItemDataSource(private val scope: CoroutineScope) : PageKeyedDataSource<Int, PopularMovieResults>() {

    private val apiService = ApiFactory.MOVIE_SERVICE

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PopularMovieResults>
    ) {
        apiService.getPopularMovies(FIRST_PAGE)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PopularMovieResults>
    ) {
        apiService.getPopularMovies(FIRST_PAGE)
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PopularMovieResults>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        const val PAGE_SIZE = 50
        const val FIRST_PAGE = 1

    }
}