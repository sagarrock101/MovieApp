package com.example.tmdb

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
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
        setImageDrawable(placeHolder)
        return
    }
    val urlWithHost =
        (if (isLarge) BuildConfig.LARGE_IMAGE_URL else BuildConfig.SMALL_IMAGE_URL) + url
    val requestBuilder = GlideApp.with(context).load(urlWithHost)
        .placeholder(R.mipmap.ic_launcher_round)
    val requestOptions = RequestOptions().diskCacheStrategy(
        if (isCacheSource) DiskCacheStrategy.DATA else DiskCacheStrategy.RESOURCE
    )
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