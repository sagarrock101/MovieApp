package com.sagaRock101.tmdb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sagaRock101.tmdb.GlideApp
import com.sagaRock101.tmdb.R
import com.sagaRock101.tmdb.api.AppConstants
import com.sagaRock101.tmdb.model.Movie

class Adapter(private val list: List<Movie>)
    : RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.movie_poster_item, parent, false)
        return  ViewHolder(view)
    }
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder:ViewHolder, position: Int ) {
        holder.bindItems(list[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view.findViewById(R.id.posterImageView)

        fun bindItems(item: Movie) {
            GlideApp.with(imageView.context)
                .load(AppConstants.IMAGE_URL + item.posterPath)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(imageView)
        }
    }


}