package com.example.tmdb

import androidx.paging.PageKeyedDataSource
import com.example.tmdb.api.TmdbService
import com.example.tmdb.model.PopularMovieResults

class PopMovieDataSource(private val tmdbService: TmdbService) : PageKeyedDataSource<Int, PopularMovieResults>(){
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PopularMovieResults>
    ) {

    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PopularMovieResults>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PopularMovieResults>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}