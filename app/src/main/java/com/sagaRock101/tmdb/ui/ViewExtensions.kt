package com.sagaRock101.tmdb.ui

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sagaRock101.tmdb.R
import com.google.android.material.snackbar.Snackbar
import kotlin.math.max

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.requestGlideListener(): RequestListener<Drawable> {
    return object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            circularRevealedAtCenter()
            return false
        }
    }
}

fun View.circularRevealedAtCenter() {
    val view = this
    val cx = (view.left + view.right) / 2
    val cy = (view.top + view.bottom) / 2
    val finalRadius = max(view.width, view.height)

    if (checkIsMaterialVersion() && view.isAttachedToWindow) {
        ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius.toFloat())
            .apply {
                view.visible()
                view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorBlack))
                duration = 550
                start()
            }
    }
}

fun checkIsMaterialVersion() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP


fun View.setupSnackbar (
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Int>,
    timeLength: Int
) {
    snackbarEvent.observe(lifecycleOwner, Observer { it ->
            this.showSnackbar(context.getString(it), timeLength)
    })
}



fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).show()
}