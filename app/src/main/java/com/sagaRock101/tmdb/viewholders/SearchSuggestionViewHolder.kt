package com.sagaRock101.tmdb.viewholders

import android.view.View
import com.sagaRock101.tmdb.databinding.ItemSearchResultBinding
import com.sagaRock101.tmdb.ui.interfaces.OnViewClickListener
import kotlinx.android.synthetic.main.item_search_result.view.*

class SearchSuggestionViewHolder(
    val binding: ItemSearchResultBinding,
    var listener: OnViewClickListener?
):
    BaseViewHolder<String>(binding), View.OnClickListener {


    override fun bind(item: String) {
        binding.item = item
    }

    override fun onClick(v: View?) {
        listener?.onClickView(v?.id, binding.item)
    }

    init {
        itemView.cl_search_item.setOnClickListener(this)
    }
}