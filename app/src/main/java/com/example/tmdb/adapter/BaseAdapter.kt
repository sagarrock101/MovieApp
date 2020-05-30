package com.example.tmdb.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.viewholders.ItemViewHolder

abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    var listItems: List<T>

    constructor(listItems: List<T>) {
        this.listItems = listItems
    }

    constructor() {
        listItems = emptyList()
    }

    fun setItems(listItems: List<T>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ItemViewHolder<T>{
        return getViewHolder(parent, viewType )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as ItemViewHolder<T>).bind(listItems[position])

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position, listItems[position])
    }

    protected abstract fun getLayoutId(position: Int, obj: T): Int

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<T>

    internal interface Binder<T> {
        fun bind(data: T)
    }

}