package com.example.tmdb.viewholders

import com.example.tmdb.databinding.MovieTrailerItemBinding
import com.example.tmdb.model.MovieTrailer

class TrailerViewHolder(val binding: MovieTrailerItemBinding):
    ItemViewHolder<MovieTrailer>(binding){
    var onTrailerItemClick: ((MovieTrailer) -> Unit)? = null
    private var data: MovieTrailer? = null
    override fun bind(item: MovieTrailer) {
        this.data = item
    }

    init {
        itemView.setOnClickListener {
            onTrailerItemClick?.invoke(data!!)
        }
    }
}