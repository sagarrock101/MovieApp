package com.example.tmdb.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.viewholders.BaseViewHolder

abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    var listItems: MutableList<T>

    constructor(listItems: MutableList<T>) {
        this.listItems = listItems
    }

    constructor() {
        listItems = emptyList<T>().toMutableList()
    }

    fun setItems(listItems: MutableList<T>) {
        this.listItems = listItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            BaseViewHolder<T>{
        return getViewHolder(parent, viewType )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as BaseViewHolder<T>).bind(listItems[position])

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutId(position, listItems[position])
    }

    protected abstract fun getLayoutId(position: Int, obj: T): Int

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T>

    internal interface Binder<T> {
        fun bind(data: T)
    }

}