package com.example.tmdb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.GlideApp
import com.example.tmdb.R
import com.example.tmdb.databinding.MovieTrailerItemBinding
import com.example.tmdb.model.MovieTrailer

class TrailerAdapter
    : RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {
    private var list: List<MovieTrailer>? = null

    var onTrailerItemClick: ((MovieTrailer) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieTrailerItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list!![position])
    }

     fun setData(listFromViewModel: List<MovieTrailer>) {
        list = listFromViewModel
    }

    inner class ViewHolder(val binding: MovieTrailerItemBinding):
        RecyclerView.ViewHolder(binding.root){

        private val MOVIE_TRAILER_THUMBNAIL_URL_PART_ONE = "https://img.youtube.com/vi/"
        private val MOVIE_TRAILER_THUMBNAIL_URL_PART_TWO = "/0.jpg"

        fun bind(data: MovieTrailer) {
            GlideApp.with(binding.ivTrailerThumbnail)
                .load(MOVIE_TRAILER_THUMBNAIL_URL_PART_ONE + data.key + MOVIE_TRAILER_THUMBNAIL_URL_PART_TWO)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(binding.ivTrailerThumbnail)
        }

        init {
            itemView.setOnClickListener {
                onTrailerItemClick?.invoke(list!![adapterPosition])
            }
        }


    }

}