package com.example.tmdb.adapter

import android.view.ViewGroup
import com.example.tmdb.R
import com.example.tmdb.Utils
import com.example.tmdb.databinding.ItemReviewBinding
import com.example.tmdb.model.Review
import com.example.tmdb.viewholders.BaseViewHolder
import com.example.tmdb.viewholders.ReviewHolder

class ReviewAdapter : BaseAdapter<Review>() {
    
    override fun getLayoutId(position: Int, obj: Review) = R.layout.item_review

    override fun getViewHolder( parent: ViewGroup, viewType: Int): BaseViewHolder<Review> {
        val binding = Utils.binder<ItemReviewBinding>(R.layout.item_review, parent)
        return ReviewHolder(binding)
    }
}