package com.example.tmdb

import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.adapter.GenericAdapter
import com.example.tmdb.databinding.MoviePosterItemBinding
import com.example.tmdb.model.Movie

class MyViewHolder(private var binding: MoviePosterItemBinding) : RecyclerView.ViewHolder(binding.root),
        GenericAdapter.Binder<Movie> {
    var onItemClick: ((Movie) -> Unit)? = null
    var posterImageView = binding.posterImageView

    override fun bind(data: Movie) {
      binding.movieItem = data
    }

}