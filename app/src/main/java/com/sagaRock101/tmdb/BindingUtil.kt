package com.sagaRock101.tmdb

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.sagaRock101.tmdb.adapter.BaseAdapter
import com.sagaRock101.tmdb.api.AppConstants
import com.sagaRock101.tmdb.model.Movie
import com.sagaRock101.tmdb.model.MovieTrailer
import com.sagaRock101.tmdb.ui.requestGlideListener
import java.io.File

@BindingAdapter(
    value = ["loadImageMovie", "placeholder", "centerCrop", "fitCenter", "circleCrop", "cacheSource", "animation", "large"],
    requireAll = false
)
fun ImageView.loadImageMovie(
    url: String? = "", placeHolder: Drawable?,
    centerCrop: Boolean = false, fitCenter: Boolean = false, circleCrop: Boolean = false,
    isCacheSource: Boolean = false, animation: Boolean = false, isLarge: Boolean = false
) {
    if (url.isNullOrBlank()) {
//        setImageDrawable(this.context.resources.getDrawable(R.mipmap.ic_launcher))
        return
    }
    var requestOptions = RequestOptions()
     requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))

    val urlWithHost =
        (if (isLarge) BuildConfig.LARGE_IMAGE_URL else BuildConfig.SMALL_IMAGE_URL) + url
    val requestBuilder = GlideApp.with(context).load(urlWithHost)
        .placeholder(R.mipmap.ic_launcher_round)
//    val requestOptions = RequestOptions().diskCacheStrategy(
//        if (isCacheSource) DiskCacheStrategy.DATA else DiskCacheStrategy.RESOURCE
//    )
        .placeholder(placeHolder)

    if (animation.not()) requestOptions.dontAnimate()
    if (centerCrop) requestOptions.centerCrop()
    if (fitCenter) requestOptions.fitCenter()
    if (circleCrop) requestOptions.circleCrop()
    val file = File(urlWithHost)
    if (file.exists()) {
        requestOptions.signature(ObjectKey(file.lastModified().toString()))
    }
    requestBuilder.apply(requestOptions).into(this)
}



@BindingAdapter("adapter")
fun RecyclerView.bindAdapter(baseAdapter: BaseAdapter<Any>) {
    this.adapter = baseAdapter
}

@BindingAdapter("thumbailKey")
fun ImageView.bindTrailerThumbnail(item: MovieTrailer) {
    GlideApp.with(this)
        .load(
            Constants.MOVIE_TRAILER_THUMBNAIL_URL_PART_ONE + item.key +
                    Constants.MOVIE_TRAILER_THUMBNAIL_URL_PART_TWO
        )
        .placeholder(R.mipmap.ic_launcher_round)
        .into(this)
}

@BindingAdapter("backdrop")
fun AppCompatImageView.bindBackDrop(movie: Movie) {
    GlideApp.with(this)
        .load(AppConstants.IMAGE_BACK_DROP + movie.backDropPath)
        .listener(this.requestGlideListener())
        .into(this)
}


