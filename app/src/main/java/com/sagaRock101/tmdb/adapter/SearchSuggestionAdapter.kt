package com.sagaRock101.tmdb.adapter

import android.view.ViewGroup
import com.sagaRock101.tmdb.R
import com.sagaRock101.tmdb.Utils
import com.sagaRock101.tmdb.databinding.ItemSearchResultBinding
import com.sagaRock101.tmdb.viewholders.BaseViewHolder
import com.sagaRock101.tmdb.viewholders.SearchSuggestionViewHolder

class SearchSuggestionAdapter : BaseAdapter<String>() {

    override fun getLayoutId(position: Int, obj: String) = R.layout.item_search_result

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
        val binding = Utils.binder<ItemSearchResultBinding>(R.layout.item_search_result, parent)
        return SearchSuggestionViewHolder(binding)
    }
}