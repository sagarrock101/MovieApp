package com.sagaRock101.tmdb.adapter

import android.view.ViewGroup
import com.sagaRock101.tmdb.R
import com.sagaRock101.tmdb.Utils
import com.sagaRock101.tmdb.databinding.ItemReviewBinding
import com.sagaRock101.tmdb.model.Review
import com.sagaRock101.tmdb.viewholders.BaseViewHolder
import com.sagaRock101.tmdb.viewholders.ReviewHolder

class ReviewAdapter : BaseAdapter<Review>() {
    
    override fun getLayoutId(position: Int, obj: Review) = R.layout.item_review

    override fun getViewHolder( parent: ViewGroup, viewType: Int): BaseViewHolder<Review> {
        val binding = Utils.binder<ItemReviewBinding>(R.layout.item_review, parent)
        return ReviewHolder(binding)
    }
}