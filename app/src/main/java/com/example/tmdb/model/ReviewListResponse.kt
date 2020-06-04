package com.example.tmdb.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewListResponse(
    @Json(name = "page")
    val page: Int,
    @Json(name = "results")
    val results: List<Review>,
    @Json(name = "total_pages")
    val total_pages: Int,
    @Json(name = "total_results")
    val total_results: Int
) {
}