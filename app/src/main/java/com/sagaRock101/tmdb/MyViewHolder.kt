package com.sagaRock101.tmdb

import androidx.recyclerview.widget.RecyclerView
import com.sagaRock101.tmdb.adapter.BaseAdapter
import com.sagaRock101.tmdb.databinding.MoviePosterItemBinding
import com.sagaRock101.tmdb.model.Movie

class MyViewHolder(private var binding: MoviePosterItemBinding) : RecyclerView.ViewHolder(binding.root),
        BaseAdapter.Binder<Movie> {
    var onItemClick: ((Movie) -> Unit)? = null
    var posterImageView = binding.posterImageView

    override fun bind(data: Movie) {
      binding.movieItem = data
    }

}