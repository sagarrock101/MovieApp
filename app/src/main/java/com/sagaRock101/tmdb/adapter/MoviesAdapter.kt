package com.sagaRock101.tmdb.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sagaRock101.tmdb.R
import com.sagaRock101.tmdb.databinding.MoviePosterItemBinding
import com.sagaRock101.tmdb.model.Movie
import com.sagaRock101.tmdb.model.NetworkState
import com.sagaRock101.tmdb.ui.fragments.MovieFragmentDirections
import com.sagaRock101.tmdb.viewholders.NetworkStateItemViewHolder


class MoviesAdapter(private val retryCallback: () -> Unit)
    : PagedListAdapter<Movie, RecyclerView.ViewHolder>(diffCallback) {

    private var TAG = "PageAdapter"

    private var networkState: NetworkState? = null

    var type = ""

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            R.layout.movie_poster_item -> {
                getItem(position)?.let { (holder as MyViewHolder).bind(it)
                    holder.posterImageView.setOnClickListener { view ->
                        try {
                            if(Navigation.findNavController(view).currentDestination?.id ==
                                R.id.movieFragment) {
                                val action = MovieFragmentDirections
                                    .actionMovieFragmentToMovieDetailFragment(getItem(position),
                                    getItem(position)?.title)
                                view.findNavController().navigate(action)
                                Log.e(TAG, "Navigation Host "
                                        + Navigation.findNavController(view).currentDestination)
                            } else {
                                Log.e(TAG, "Navigation Host "
                                        + Navigation.findNavController(view).currentDestination?.id)
                            }

                        }catch (e: Exception) {
                            Log.e(TAG, "Position: $position")
                            Log.e(TAG, "Error Message: " + e.message)
                            if(Navigation.findNavController(view).currentDestination?.id ==
                                R.id.movieFragment) {
                                if(position == currentList?.size) {
                                    val action = MovieFragmentDirections
                                        .actionMovieFragmentToMovieDetailFragment(getItem(position-1))
                                    view.findNavController().navigate(action)
                                } else {
                                    if(position < currentList!!.size) {
                                        val action = MovieFragmentDirections
                                            .actionMovieFragmentToMovieDetailFragment(getItem(position))
                                        view.findNavController().navigate(action)
                                    } else {
                                        val action =
                                            MovieFragmentDirections
                                                .actionMovieFragmentToMovieDetailFragment(getItem(0))
                                        view.findNavController().navigate(action)
                                    }
                                }
                            } else {
                                Log.e(TAG, "Navigation Host "
                                        + Navigation.findNavController(view).currentDestination?.id)
                            }

                        }
                    }}
            }
            R.layout.component_network_state_item -> (holder as NetworkStateItemViewHolder).bindTo(
                networkState)


        }

    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.component_network_state_item
        } else {
            R.layout.movie_poster_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.movie_poster_item -> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = MoviePosterItemBinding.inflate(inflater, parent, false)
                MyViewHolder(binding)
            }
            R.layout.component_network_state_item -> NetworkStateItemViewHolder.create(parent,retryCallback)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
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

    inner class MyViewHolder(private var binding: MoviePosterItemBinding)
        : RecyclerView.ViewHolder(binding.root),
        BaseAdapter.Binder<Movie> {
        var onItemClick: ((Movie) -> Unit)? = null
        var posterImageView = binding.posterImageView

        override fun bind(data: Movie) {
            binding.movieItem = data
        }

    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}