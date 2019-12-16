package com.example.tmdb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.BR
import com.example.tmdb.databinding.MoviePosterItemBinding
import com.example.tmdb.model.MovieResults

class MovieAdapter(private val list: List<MovieResults>,
                   private val lifecycleOwner: LifecycleOwner)
    : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MoviePosterItemBinding.inflate(inflater)
        binding.lifecycleOwner = lifecycleOwner
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        holder.binding.setVariable(BR.movieItem, list[position])

    }

    class ViewHolder( val binding: MoviePosterItemBinding)
        :RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieResults) {
//            GlideApp.with(binding.posterImageView)
//                .load(AppConstants.IMAGE_URL + item.posterPath)
//                .placeholder(R.mipmap.ic_launcher_round)
//                .into(binding.posterImageView)
        }
    }

}