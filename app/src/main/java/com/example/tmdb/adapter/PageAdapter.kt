package com.example.tmdb.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.R
import com.example.tmdb.databinding.MoviePosterItemBinding
import com.example.tmdb.model.Movie
import com.example.tmdb.ui.fragments.MovieDetailFragment

class PageAdapter(var supportFragmentManager: FragmentManager)
    : PagedListAdapter<Movie, PageAdapter.MyViewHolder>(diffCallback) {

    var onItemClick: ((Movie) -> Unit)? = null
    private var TAG = "PageAdapter"

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
        holder.posterImageView.setOnClickListener {
            val args = Bundle()
            var fragment = MovieDetailFragment()
            fragment.arguments = args
            args.putParcelable("data", getItem(position))
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_main, fragment)
                .addToBackStack(null)
                .commit()
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
        private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            /**
             * Note that in kotlin, == checking on data classes compares all contents, but in Java,
             * typically you'll implement Object#equals, and use it to compare object contents.
             */
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem

        }
    }

    inner class MyViewHolder(private var binding: MoviePosterItemBinding) : RecyclerView.ViewHolder(binding.root),
        GenericAdapter.Binder<Movie> {
        var onItemClick: ((Movie) -> Unit)? = null
        var posterImageView = binding.posterImageView

        override fun bind(data: Movie) {
            binding.movieItem = data
        }


    }

}