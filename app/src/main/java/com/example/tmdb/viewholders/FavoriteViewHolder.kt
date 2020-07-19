package com.example.tmdb.viewholders

import android.view.View
import com.example.tmdb.databinding.MoviePosterItemBinding
import com.example.tmdb.model.Movie
import com.example.tmdb.ui.interfaces.OnFavoriteClick

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