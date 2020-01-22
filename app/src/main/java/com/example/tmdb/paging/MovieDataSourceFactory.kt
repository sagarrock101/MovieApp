package com.example.tmdb.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.tmdb.api.TmdbService
import com.example.tmdb.model.Movie
import com.example.tmdb.model.MovieSearch

class PopularMoviesDataSourceFactory(movieApi: TmdbService, private val search: MovieSearch) :
    DataSource.Factory<Int?, Movie?>() {
    private val movieApi: TmdbService = movieApi
    val moviesDataSource: MutableLiveData<MovieDataSource> =
        MutableLiveData()

    override fun create(): DataSource<Int?, Movie?> {
        val dataSource = MovieDataSource(movieApi, search)
        moviesDataSource.postValue(dataSource)
        return dataSource
    }

    fun getPopularMoviesDataSource(): MutableLiveData<MovieDataSource> {
        return moviesDataSource
    }

}