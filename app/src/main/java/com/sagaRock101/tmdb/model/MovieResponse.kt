package com.sagaRock101.tmdb.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieResponse (
    val page: String,
    val results: List<Movie>,
    @Json(name = "total_pages")
     val totalPages: Int
)