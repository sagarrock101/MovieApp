package com.sagaRock101.tmdb.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.sagaRock101.tmdb.api.TmdbService
import com.sagaRock101.tmdb.model.Movie

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