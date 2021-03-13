package com.sagaRock101.tmdb.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Keyword(
    @Json(name = "id")
    var id: Int,
    @Json(name = "name")
    var name: String
)