package com.example.tmdb

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.adapter.GenericAdapter
import com.example.tmdb.api.AppConstants
import com.example.tmdb.databinding.MoviePosterItemBinding
import com.example.tmdb.model.PopularMovieResults

class MyViewHolder(private var binding: MoviePosterItemBinding) : RecyclerView.ViewHolder(binding.root),
        GenericAdapter.Binder<PopularMovieResults> {
    override fun bind(data: PopularMovieResults) {
        GlideApp.with(binding.posterImageView)
            .load(AppConstants.IMAGE_URL + data.posterPath)
            .placeholder(R.mipmap.ic_launcher_round)
            .into(binding.posterImageView)
    }

}