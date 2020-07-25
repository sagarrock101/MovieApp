package com.example.tmdb.viewholders

import com.example.tmdb.databinding.ItemSearchResultBinding

class SearchSuggestionViewHolder(val binding: ItemSearchResultBinding):
    BaseViewHolder<String>(binding){
    override fun bind(item: String) {
        binding.item = item
    }
}