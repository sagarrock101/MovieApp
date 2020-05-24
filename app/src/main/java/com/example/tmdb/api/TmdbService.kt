package com.example.tmdb.api

import com.example.tmdb.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    @GET("movie/{id}")
    fun getMovies(
        @Path("id") movieId: String,
        @Query("page") page: Int
    ) : Call<MovieResponse>

    @GET("movie/{movie_id}/videos")
    fun getTrailerList(@Path("movie_id") movieId: Int): Call<MovieTrailerResponse>

    @GET("movie/{movie_id}/reviews")
    fun getReviews(@Path("movie_id") movieId: Int) : Call<ReviewListResponse>
}