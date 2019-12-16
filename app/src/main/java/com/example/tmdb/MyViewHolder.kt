package com.example.tmdb

import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.adapter.GenericAdapter
import com.example.tmdb.databinding.MoviePosterItemBinding
import com.example.tmdb.model.MovieResults

class MyViewHolder(private var binding: MoviePosterItemBinding) : RecyclerView.ViewHolder(binding.root),
        GenericAdapter.Binder<MovieResults> {
    override fun bind(data: MovieResults) {
      binding.movieItem = data
    }

}