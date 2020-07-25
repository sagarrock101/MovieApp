package com.sagaRock101.tmdb.viewholders

import android.view.View
import com.sagaRock101.tmdb.databinding.MoviePosterItemBinding
import com.sagaRock101.tmdb.model.Movie
import com.sagaRock101.tmdb.ui.interfaces.OnFavoriteClick

class FavoriteViewHolder(val binding: MoviePosterItemBinding,
                         private var onFavoriteClick: OnFavoriteClick):
    BaseViewHolder<Movie>(binding), View.OnClickListener {
    override fun bind(item: Movie) {
        binding.movieItem = item
    }

    override fun onClick(v: View?) {
        onFavoriteClick.onFavoriteClicked(binding.movieItem!!)
    }

    init {
        itemView.setOnClickListener(this)
    }
}