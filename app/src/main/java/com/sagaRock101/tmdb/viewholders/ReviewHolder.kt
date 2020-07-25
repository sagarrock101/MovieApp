package com.sagaRock101.tmdb.viewholders

import com.sagaRock101.tmdb.databinding.ItemReviewBinding
import com.sagaRock101.tmdb.model.Review

class ReviewHolder(val binding: ItemReviewBinding):
    BaseViewHolder<Review>(binding) {
    override fun bind(item: Review) {
        binding.itemReviewContent.text = item.content
       binding.item = item
    }
}