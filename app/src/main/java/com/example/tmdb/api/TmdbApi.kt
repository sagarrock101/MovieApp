package com.example.tmdb.api

import com.example.tmdb.model.PopularMovieResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface TmdbApi {

    @GET("movie/popular")
    fun getPopularMovies() : Deferred<Response<PopularMovieResponse>>

}