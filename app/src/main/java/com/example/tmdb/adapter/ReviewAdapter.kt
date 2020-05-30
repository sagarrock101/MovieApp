package com.example.tmdb.adapter

import android.view.ViewGroup
import com.example.tmdb.R
import com.example.tmdb.Utils
import com.example.tmdb.databinding.ItemReviewBinding
import com.example.tmdb.databinding.MovieTrailerItemBinding
import com.example.tmdb.model.MovieTrailer
import com.example.tmdb.model.Review
import com.example.tmdb.viewholders.ItemViewHolder
import com.example.tmdb.viewholders.ReviewHolder
import com.example.tmdb.viewholders.TrailerViewHolder

class ReviewAdapter : BaseAdapter<Review>() {
    
    override fun getLayoutId(position: Int, obj: Review) = R.layout.item_review

    override fun getViewHolder( parent: ViewGroup, viewType: Int): ItemViewHolder<Review> {
        val binding = Utils.binder<ItemReviewBinding>(R.layout.item_review, parent)
        return ReviewHolder(binding)
    }
}