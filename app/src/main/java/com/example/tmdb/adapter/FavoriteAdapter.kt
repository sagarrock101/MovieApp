package com.example.tmdb.adapter

import android.view.ViewGroup
import com.example.tmdb.R
import com.example.tmdb.Utils
import com.example.tmdb.databinding.MoviePosterItemBinding
import com.example.tmdb.model.Movie
import com.example.tmdb.ui.interfaces.OnFavoriteClick
import com.example.tmdb.viewholders.BaseViewHolder
import com.example.tmdb.viewholders.FavoriteViewHolder

class FavoriteAdapter : BaseAdapter<Movie>() {
    override fun getLayoutId(position: Int, obj: Movie) = R.layout.movie_poster_item

    var listener: OnFavoriteClick? = null

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Movie> {
        val binding = Utils.binder<MoviePosterItemBinding>(R.layout.movie_poster_item, parent)
        return FavoriteViewHolder(binding, listener!!)
    }

    fun setClickListener(listener: OnFavoriteClick) {
        this.listener = listener
    }
}