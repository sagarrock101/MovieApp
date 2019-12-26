package com.example.tmdb.Paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.tmdb.api.TmdbService
import com.example.tmdb.model.MovieResults
import com.example.tmdb.model.MovieSearch

class PopularMoviesDataSourceFactory(movieApi: TmdbService, private val search: MovieSearch) :
    DataSource.Factory<Int?, MovieResults?>() {
    private val movieApi: TmdbService = movieApi
    private val moviesDataSource: MutableLiveData<MovieDataSource> =
        MutableLiveData()

    override fun create(): DataSource<Int?, MovieResults?> {
        val dataSource = MovieDataSource(movieApi, search)
        moviesDataSource.postValue(dataSource)
        return dataSource
    }

    fun getPopularMoviesDataSource(): MutableLiveData<MovieDataSource> {
        return moviesDataSource
    }

}