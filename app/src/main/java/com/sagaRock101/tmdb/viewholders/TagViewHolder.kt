package com.sagaRock101.tmdb.viewholders

import com.sagaRock101.tmdb.databinding.ItemTagBinding
import com.sagaRock101.tmdb.model.Keyword

class TagViewHolder(
    val binding: ItemTagBinding
): BaseViewHolder<Keyword>(binding) {

    override fun bind(item: Keyword) {
        binding.tvTag.text = item.name
    }
}