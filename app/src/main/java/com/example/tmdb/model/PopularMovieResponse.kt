package com.example.tmdb.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PopularMovieResponse (
     val page: String,
     val results: List<PopularMovieResults>,
     @Json(name = "total_pages")
     val totalPages: Int
)