package com.sagaRock101.tmdb.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MovieTrailer(
  @Json(name = "key")
  var key: String? = null,
  @Json(name = "name")
  var name: String? = null
) : Parcelable
