package com.example.tmdb.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.MyViewHolder
import com.example.tmdb.R
import com.example.tmdb.databinding.MoviePosterItemBinding
import com.example.tmdb.model.MovieResults
import com.example.tmdb.ui.MainActivity
import com.example.tmdb.ui.fragments.MovieFragment

class PageAdapter
    : PagedListAdapter<MovieResults, PageAdapter.MyViewHolder>(diffCallback) {

    var onItemClick: ((MovieResults) -> Unit)? = null
    private var TAG = "PageAdapter"

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
        holder.posterImageView.setOnClickListener {view ->
            view.findNavController().navigate(R.id.action_movieFragment_to_movieDetailFragment)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MoviePosterItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }




    companion object {
        /**
         * This diff callback informs the PagedListAdapter how to compute list differences when new
         * PagedLists arrive.
         * <p>
         *
         * @see android.support.v7.util.DiffUtil
         */
        private val diffCallback = object : DiffUtil.ItemCallback<MovieResults>() {
            override fun areItemsTheSame(oldItem: MovieResults, newItem: MovieResults): Boolean =
                oldItem.id == newItem.id

            /**
             * Note that in kotlin, == checking on data classes compares all contents, but in Java,
             * typically you'll implement Object#equals, and use it to compare object contents.
             */
            override fun areContentsTheSame(oldItem: MovieResults, newItem: MovieResults): Boolean =
                oldItem == newItem

        }
    }

    inner class MyViewHolder(private var binding: MoviePosterItemBinding) : RecyclerView.ViewHolder(binding.root),
        GenericAdapter.Binder<MovieResults> {
        var onItemClick: ((MovieResults) -> Unit)? = null
        var posterImageView = binding.posterImageView

        override fun bind(data: MovieResults) {
            binding.movieItem = data
        }

        init {
            itemView.setOnClickListener {
                Log.e(TAG,"Clicked")
            }
        }
    }

}