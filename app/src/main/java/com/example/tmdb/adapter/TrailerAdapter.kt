package com.example.tmdb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.GlideApp
import com.example.tmdb.R
import com.example.tmdb.databinding.MovieTrailerItemBinding
import com.example.tmdb.model.MovieTrailer

class TrailerAdapter(private val list: List<MovieTrailer>)
    : RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieTrailerItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(val binding: MovieTrailerItemBinding):
        RecyclerView.ViewHolder(binding.root),View.OnClickListener {

        private val MOVIE_TRAILER_THUMBNAIL_URL_PART_ONE = "https://img.youtube.com/vi/"
        private val MOVIE_TRAILER_THUMBNAIL_URL_PART_TWO = "/0.jpg"

        fun bind(data: MovieTrailer) {
            GlideApp.with(binding.ivTrailerThumbnail)
                .load(MOVIE_TRAILER_THUMBNAIL_URL_PART_ONE + data.key + MOVIE_TRAILER_THUMBNAIL_URL_PART_TWO)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(binding.ivTrailerThumbnail)
        }



        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

}