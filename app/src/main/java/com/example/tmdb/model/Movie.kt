package com.example.tmdb.model

import android.os.Parcelable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "movie_table")
@Parcelize
@JsonClass(generateAdapter = true)
data class Movie(

    @PrimaryKey
    @Json(name = "id")
    val id: Int? = null,

    @ColumnInfo
    @Json(name = "vote_average")
    val vote_average: Double? = null,

    @ColumnInfo
    @Json(name = "title")
    val title: String? = null,

    @ColumnInfo
    @Json(name = "overview")
    val overview: String? = null,

    @ColumnInfo
    @Json(name = "adult")
    val adult: Boolean? = null,

    @ColumnInfo
    @Json(name = "poster_path")
    val posterPath: String? = null,

    @ColumnInfo
    @Json(name = "backdrop_path")
    val backDropPath: String? = null
) : Parcelable {
    @BindingAdapter("poster_path")
    fun loadImage(view: ImageView, imageUrl: String) {
        Glide.with(view.context)
            .load(imageUrl)
            .apply(RequestOptions().circleCrop())
            .into(view)
    }
}