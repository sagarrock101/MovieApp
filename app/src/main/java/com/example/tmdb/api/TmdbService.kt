package com.example.tmdb.api

import com.example.tmdb.model.PopularMovieResponse
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbService {

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("page") page: Int
    ) : Call<PopularMovieResponse>

}