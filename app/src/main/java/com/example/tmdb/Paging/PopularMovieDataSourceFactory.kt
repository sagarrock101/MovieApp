package com.example.tmdb.Paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.tmdb.api.TmdbService
import com.example.tmdb.model.PopularMovieResults

class PopularMoviesDataSourceFactory(movieApi: TmdbService) :
    DataSource.Factory<Int?, PopularMovieResults?>() {
    private val movieApi: TmdbService
    private val popularMoviesDataSource: MutableLiveData<PopularMovieDataSource> =
        MutableLiveData<PopularMovieDataSource>()

    override fun create(): DataSource<Int?, PopularMovieResults?> {
        val dataSource = PopularMovieDataSource(movieApi)
        popularMoviesDataSource.postValue(dataSource)
        return dataSource
    }

    fun getPopularMoviesDataSource(): MutableLiveData<PopularMovieDataSource> {
        return popularMoviesDataSource
    }

    init {
        this.movieApi = movieApi
    }
}