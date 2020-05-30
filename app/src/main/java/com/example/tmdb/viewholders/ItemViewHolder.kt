package com.example.tmdb.viewholders

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


abstract class ItemViewHolder<T>(binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item: T)
}