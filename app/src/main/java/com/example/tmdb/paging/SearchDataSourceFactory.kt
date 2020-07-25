package com.example.tmdb.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.tmdb.api.TmdbService
import com.example.tmdb.model.Movie

class SearchDataSourceFactory(movieApi: TmdbService, private val search: String)
    : DataSource.Factory<Int?, Movie?>() {
    private val movieApi: TmdbService = movieApi
    val moviesDataSource: MutableLiveData<SearchDataSource> =
        MutableLiveData()
    override fun create(): DataSource<Int?, Movie?> {
        val dataSource = SearchDataSource(movieApi, search)
        moviesDataSource.postValue(dataSource)
        return dataSource
    }

}