package com.example.tmdb.viewholders

import android.view.View
import com.example.tmdb.databinding.MovieTrailerItemBinding
import com.example.tmdb.model.MovieTrailer
import com.example.tmdb.ui.interfaces.OnTrailerClickListener

class TrailerViewHolder(val binding: MovieTrailerItemBinding,
                        var onTrailerClickListener: OnTrailerClickListener
):
    BaseViewHolder<MovieTrailer>(binding), View.OnClickListener {
    private var listener: OnTrailerClickListener? = null
    private var item: MovieTrailer? = null
    override fun bind(item: MovieTrailer) {
        listener = onTrailerClickListener
        this.item = item
        binding.trailerItem = item
    }

    override fun onClick(v: View?) {
        listener?.onTrailerClicked(item!!)
    }

    init {
        itemView.setOnClickListener(this)
    }
}