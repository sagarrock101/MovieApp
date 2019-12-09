package com.example.tmdb.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PopularMovieResults(
    val id: Int? = null,
    val vote_average: Double? = null,
    val title: String? = null,
    val overview: String? = null,
    val adult: Boolean? = null,
    @Json(name = "poster_path")
    val posterPath: String? = null,
    @Json(name = "backdrop_path")
    val backDropPath: String? = null
) {
    @BindingAdapter("poster_path")
    fun loadImage(view: ImageView, imageUrl: String) {
        Glide.with(view.context)
            .load(imageUrl)
            .apply(RequestOptions().circleCrop())
            .into(view)
    }
}