package com.example.tmdb.adapter

import android.view.ViewGroup
import com.example.tmdb.R
import com.example.tmdb.Utils
import com.example.tmdb.databinding.ItemSearchResultBinding
import com.example.tmdb.viewholders.BaseViewHolder
import com.example.tmdb.viewholders.SearchSuggestionViewHolder

class SearchSuggestionAdapter : BaseAdapter<String>() {
    
    override fun getLayoutId(position: Int, obj: String) = R.layout.item_search_result

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
        val binding = Utils.binder<ItemSearchResultBinding>(R.layout.item_search_result, parent)
        return SearchSuggestionViewHolder(binding)
    }
}