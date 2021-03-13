package com.sagaRock101.tmdb.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Review(
    @Json(name = "author")
    var author: String,
    @Json(name = "content")
    var content: String,
    @Json(name = "url")
    val url: String
) {
}