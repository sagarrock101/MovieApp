package com.example.tmdb.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieTrailerResponse(
    @Json(name = "id")
    var id: Int? = null,
    @Json(name = "results")
    var results: List<MovieTrailer>
)