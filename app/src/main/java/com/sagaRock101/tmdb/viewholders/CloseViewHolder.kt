package com.sagaRock101.tmdb.viewholders

import android.view.View
import com.sagaRock101.tmdb.databinding.ItemSuggestionCloseBinding
import com.sagaRock101.tmdb.ui.interfaces.CloseBtnClickListener
import kotlinx.android.synthetic.main.item_suggestion_close.view.*

class CloseViewHolder(
    val binding: ItemSuggestionCloseBinding,
    val listener: CloseBtnClickListener?
): BaseViewHolder<String>(binding), View.OnClickListener{
    override fun bind(item: String) {
    }

    override fun onClick(v: View?) {
        listener?.onCloseClick()
    }

    init {
        itemView.iv_close.setOnClickListener(this)
    }

}