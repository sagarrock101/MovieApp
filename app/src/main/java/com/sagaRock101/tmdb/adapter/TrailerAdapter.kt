package com.sagaRock101.tmdb.adapter

import android.view.ViewGroup
import com.sagaRock101.tmdb.R
import com.sagaRock101.tmdb.Utils
import com.sagaRock101.tmdb.databinding.MovieTrailerItemBinding
import com.sagaRock101.tmdb.model.MovieTrailer
import com.sagaRock101.tmdb.ui.interfaces.OnTrailerClickListener
import com.sagaRock101.tmdb.viewholders.BaseViewHolder
import com.sagaRock101.tmdb.viewholders.TrailerViewHolder

class TrailerAdapter : BaseAdapter<MovieTrailer>(){

    var listener: OnTrailerClickListener? = null

    override fun getLayoutId(position: Int, obj: MovieTrailer) = R.layout.movie_trailer_item

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MovieTrailer> {
        val binding = Utils.binder<MovieTrailerItemBinding>(R.layout.movie_trailer_item, parent)
        return TrailerViewHolder(binding, listener!!)
    }

    fun setTrailerListener(listener: OnTrailerClickListener) {
        this.listener = listener
    }
}