package com.example.tmdb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.tmdb.MyViewHolder
import com.example.tmdb.databinding.MoviePosterItemBinding
import com.example.tmdb.model.PopularMovieResults

class PageAdapter : PagedListAdapter<PopularMovieResults, MyViewHolder>(diffCallback) {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MoviePosterItemBinding.inflate(inflater)
        return MyViewHolder(binding)
    }




    companion object {
        /**
         * This diff callback informs the PagedListAdapter how to compute list differences when new
         * PagedLists arrive.
         * <p>
         * When you add a Cheese with the 'Add' button, the PagedListAdapter uses diffCallback to
         * detect there's only a single item difference from before, so it only needs to animate and
         * rebind a single view.
         *
         * @see android.support.v7.util.DiffUtil
         */
        private val diffCallback = object : DiffUtil.ItemCallback<PopularMovieResults>() {
            override fun areItemsTheSame(oldItem: PopularMovieResults, newItem: PopularMovieResults): Boolean =
                oldItem.id == newItem.id

            /**
             * Note that in kotlin, == checking on data classes compares all contents, but in Java,
             * typically you'll implement Object#equals, and use it to compare object contents.
             */
            override fun areContentsTheSame(oldItem: PopularMovieResults, newItem: PopularMovieResults): Boolean =
                oldItem == newItem

        }
    }
}