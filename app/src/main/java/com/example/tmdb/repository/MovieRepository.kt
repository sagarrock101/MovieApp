package com.example.tmdb.repository

import com.example.tmdb.api.TmdbApi
import com.example.tmdb.model.PopularMovieResults

class MovieRepository(private val api : TmdbApi) : BaseRepository() {

     suspend fun getPopularMovies() : List<PopularMovieResults>?{

        val movieResponse = safeApiCall(
            call = {api.getPopularMovies().await()},
            errorMessage = "Error Fetching Popular Movies"
        )

        return movieResponse?.results?.toList()

    }

}