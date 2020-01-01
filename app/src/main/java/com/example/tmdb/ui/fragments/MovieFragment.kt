package com.example.tmdb.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tmdb.R
import com.example.tmdb.adapter.PageAdapter
import com.example.tmdb.databinding.FragmentMovieBinding
import com.example.tmdb.model.NetworkStatus
import com.example.tmdb.viewmodel.MoviesViewModel
import com.example.tmdb.viewmodel.ViewModelFactory

class MovieFragment : Fragment() {
    private lateinit var viewModel : MoviesViewModel
    private lateinit var binding: FragmentMovieBinding
    private lateinit var adapter: PageAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var viewModelFactory: ViewModelFactory
    var page = 1
    var movieType = "popular"
    val TAG = "MovieFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProviders.of(activity!!).get(MoviesViewModel::class.java)
        viewModelFactory = ViewModelFactory(activity!!.application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MoviesViewModel::class.java)
        if(savedInstanceState == null) {
            viewModel.fetchMovies(page, movieType)
        } else {
            Log.e(TAG, "onSaveInstancState: $savedInstanceState")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false)
        swipeRefreshLayout = binding.swipeToRefresh
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchMovies(page, movieType)
            loadPopular()
        }
        val layoutManager = GridLayoutManager(context, 2)


        binding.recyclerView.layoutManager = layoutManager
        loadPopular()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun loadPopular() {
        swipeRefreshLayout.isRefreshing  = false
        var fm = (activity as AppCompatActivity).supportFragmentManager
        adapter = PageAdapter(fm)

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
            R.id.popular -> {
                movieType = "popular"
                viewModel.fetchMovies(page, movieType)
            }
            R.id.up_coming -> {
                movieType = "upcoming"
                viewModel.fetchMovies(page, movieType)
            }
            R.id.top_rated -> {
                movieType = "top_rated"
                viewModel.fetchMovies(page, movieType)
            }
            R.id.menu_favorites -> {
                favoritesLoader()
            }
            R.id.menu_refresh -> {
                swipeRefreshLayout.isRefreshing = true
                loadPopular()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun favoritesLoader() {
        viewModel.loadFavorites().observe(this, Observer { data ->
            adapter.submitList(data)
        })
    }

}