package com.sagaRock101.tmdb.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieKeywords(
    @Json(name = "id")
    var id: Int,
    @Json(name = "keywords")
    var keywords: List<Keyword>
)

