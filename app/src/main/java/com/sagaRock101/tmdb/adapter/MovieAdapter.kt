package com.sagaRock101.tmdb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.sagaRock101.tmdb.BR
import com.sagaRock101.tmdb.databinding.MoviePosterItemBinding
import com.sagaRock101.tmdb.model.Movie

class MovieAdapter(private val list: List<Movie>,
                   private val lifecycleOwner: LifecycleOwner)
    : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MoviePosterItemBinding.inflate(inflater, parent, false)
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
        fun bind(item: Movie) {
    //            GlideApp.with(binding.posterImageView)
    //                .load(AppConstants.IMAGE_URL + item.posterPath)
    //                .placeholder(R.mipmap.ic_launcher_round)
    //                .into(binding.posterImageView)
        }
    }

}