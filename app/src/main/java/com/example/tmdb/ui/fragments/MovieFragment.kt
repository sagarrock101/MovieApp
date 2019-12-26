package com.example.tmdb.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tmdb.R
import com.example.tmdb.adapter.PageAdapter
import com.example.tmdb.databinding.FragmentMovieBinding
import com.example.tmdb.model.NetworkStatus
import com.example.tmdb.viewmodel.MoviesViewModel

class MovieFragment : Fragment() {
    private lateinit var viewModel : MoviesViewModel
    private lateinit var binding: FragmentMovieBinding
    private lateinit var adapter: PageAdapter
    var page = 1
    var movieType = "popular"
    val TAG = "MovieFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false)
        viewModel = ViewModelProviders.of(activity!!).get(MoviesViewModel::class.java)
        val layoutManager = GridLayoutManager(context, 2)
        if(savedInstanceState == null) {
            viewModel.fetchMovies(page, movieType)
        }
        binding.recyclerView.layoutManager = layoutManager
        loadPopular()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun loadPopular() {

        adapter = PageAdapter()

        viewModel.popularMoviesLiveData.observe(this, Observer { data ->
            Log.e(TAG, "Size: " + data.size)
            adapter.submitList(data)
        })
        binding.recyclerView.adapter = adapter

        viewModel.getMoviesStatus().observe(this, Observer { networkStatus ->
            when(networkStatus) {
                NetworkStatus.SUCCESS -> {
                    binding.itemProgressBar.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
                NetworkStatus.LOADING -> {
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.itemProgressBar.visibility = View.VISIBLE
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.popular -> movieType = "popular"
            R.id.up_coming -> movieType = "upcoming"
            R.id.top_rated -> movieType = "top_rated"
        }
        viewModel.fetchMovies(page, movieType)
        return super.onOptionsItemSelected(item)
    }

}