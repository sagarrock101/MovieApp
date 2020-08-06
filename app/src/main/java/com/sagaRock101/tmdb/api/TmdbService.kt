package com.sagaRock101.tmdb.api

import com.sagaRock101.tmdb.model.*
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

    @GET("search/movie")
    fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Call<MovieResponse>

    @GET("search/movie")
    fun searchSuggestion(
        @Query("query") query: String
    ): Call<MovieResponse>

    @GET("movie/{movie_id}/keywords")
    fun getKeywords(
        @Path("movie_id") movieId: Int
    ): Call<MovieKeywords>
}