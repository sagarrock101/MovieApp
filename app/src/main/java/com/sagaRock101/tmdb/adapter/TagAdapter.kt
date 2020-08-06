package com.sagaRock101.tmdb.adapter

import android.view.ViewGroup
import com.sagaRock101.tmdb.R
import com.sagaRock101.tmdb.Utils
import com.sagaRock101.tmdb.databinding.ItemTagBinding
import com.sagaRock101.tmdb.model.Keyword
import com.sagaRock101.tmdb.viewholders.BaseViewHolder
import com.sagaRock101.tmdb.viewholders.TagViewHolder

class TagAdapter : BaseAdapter<Keyword>() {
    var layout = R.layout.item_tag
    override fun getLayoutId(position: Int, obj: Keyword) = layout

    override fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Keyword> {
        val binding = Utils.binder<ItemTagBinding>(layout, parent)
        return TagViewHolder(binding)
    }
}