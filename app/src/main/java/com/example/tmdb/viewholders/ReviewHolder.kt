package com.example.tmdb.viewholders

import com.example.tmdb.databinding.ItemReviewBinding
import com.example.tmdb.model.Review

class ReviewHolder(val binding: ItemReviewBinding):
    BaseViewHolder<Review>(binding) {
    override fun bind(item: Review) {
        binding.itemReviewContent.text = item.content
       binding.item = item
    }
}