package com.sagaRock101.tmdb.viewholders

import com.sagaRock101.tmdb.databinding.ItemSearchResultBinding

class SearchSuggestionViewHolder(val binding: ItemSearchResultBinding):
    BaseViewHolder<String>(binding){
    override fun bind(item: String) {
        binding.item = item
    }
}