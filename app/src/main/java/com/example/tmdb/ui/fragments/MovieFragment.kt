package com.example.tmdb.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tmdb.R
import com.example.tmdb.adapter.PageAdapter
import com.example.tmdb.databinding.FragmentMovieBinding
import com.example.tmdb.model.NetworkStatus
import com.example.tmdb.viewmodel.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_movie_detail.*

class MovieFragment : Fragment() {
    private lateinit var viewModel : MoviesViewModel
    private lateinit var binding: FragmentMovieBinding
    private lateinit var adapter: PageAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
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
        swipeRefreshLayout = binding.swipeToRefresh
        swipeRefreshLayout.setOnRefreshListener {
            Toast.makeText(context, "onRefresh",Toast.LENGTH_SHORT).show()
            viewModel.fetchMovies(page, movieType)
            loadPopular()
        }
        val layoutManager = GridLayoutManager(context, 2)
        if(savedInstanceState == null) {
            viewModel.fetchMovies(page, movieType)
        } else {
            Log.e(TAG, "onSaveInstancState: $savedInstanceState")
        }
        binding.recyclerView.layoutManager = layoutManager
        loadPopular()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun loadPopular() {
        swipeRefreshLayout.isRefreshing  = false
        adapter = PageAdapter()

        viewModel.popularMoviesLiveData.observe(this, Observer { data ->
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
            R.id.menu_refresh -> {
                swipeRefreshLayout.isRefreshing = true
                loadPopular()
            }
        }
        viewModel.fetchMovies(page, movieType)
        return super.onOptionsItemSelected(item)
    }



}