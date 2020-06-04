package com.example.tmdb.adapter

import android.view.ViewGroup
import com.example.tmdb.R
import com.example.tmdb.Utils
import com.example.tmdb.databinding.MovieTrailerItemBinding
import com.example.tmdb.model.MovieTrailer
import com.example.tmdb.ui.interfaces.OnTrailerClickListener
import com.example.tmdb.viewholders.BaseViewHolder
import com.example.tmdb.viewholders.TrailerViewHolder

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