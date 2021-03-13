package com.sagaRock101.tmdb.adapter

import android.view.ViewGroup
import com.sagaRock101.tmdb.R
import com.sagaRock101.tmdb.Utils
import com.sagaRock101.tmdb.databinding.MoviePosterItemBinding
import com.sagaRock101.tmdb.model.Movie
import com.sagaRock101.tmdb.ui.interfaces.OnFavoriteClick
import com.sagaRock101.tmdb.viewholders.BaseViewHolder
import com.sagaRock101.tmdb.viewholders.FavoriteViewHolder

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