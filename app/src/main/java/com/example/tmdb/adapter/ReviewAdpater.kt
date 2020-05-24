package com.example.tmdb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.databinding.ItemReviewBinding
import com.example.tmdb.model.MovieTrailer
import com.example.tmdb.model.Review

class ReviewAdpater
    : RecyclerView.Adapter<ReviewAdpater.ViewHolder>() {
    private var list: List<Review>? = null

    var onTrailerItemClick: ((MovieTrailer) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list!![position])
    }

     fun setData(listFromViewModel: List<Review>) {
        list = listFromViewModel
    }

    inner class ViewHolder(val binding: ItemReviewBinding):
        RecyclerView.ViewHolder(binding.root){


        fun bind(data: Review) {
            binding.itemReviewTitle.text = data.author
            binding.itemReviewContent.text = data.content
        }

        init {
            itemView.setOnClickListener {
//                onTrailerItemClick?.invoke(list!![adapterPosition])
            }
        }


    }

}