package com.sagaRock101.tmdb.adapter

import android.view.ViewGroup
import com.sagaRock101.tmdb.R
import com.sagaRock101.tmdb.Utils
import com.sagaRock101.tmdb.databinding.ItemSearchResultBinding
import com.sagaRock101.tmdb.databinding.ItemSuggestionCloseBinding
import com.sagaRock101.tmdb.ui.interfaces.CloseBtnClickListener
import com.sagaRock101.tmdb.viewholders.BaseViewHolder
import com.sagaRock101.tmdb.viewholders.CloseViewHolder
import com.sagaRock101.tmdb.viewholders.SearchSuggestionViewHolder

private const val TYPE_RESULT = 0
private const val TYPE_CLOSE = 1

class SearchSuggestionAdapter : BaseAdapter<String>() {

    private var listener: CloseBtnClickListener? = null

    override fun getLayoutId(position: Int, obj: String) = R.layout.item_search_result

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
        return when (viewType) {
            TYPE_RESULT -> {
                val binding =
                    Utils.binder<ItemSearchResultBinding>(R.layout.item_search_result, parent)
                SearchSuggestionViewHolder(binding)
            }
            else -> {
                val binding =
                    Utils.binder<ItemSuggestionCloseBinding>(R.layout.item_suggestion_close, parent)
                CloseViewHolder(binding, listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            listItems.size - 1 -> TYPE_CLOSE
            else -> TYPE_RESULT
        }
    }

    fun setCloseListener(listener: CloseBtnClickListener) {
        this.listener = listener
    }

}