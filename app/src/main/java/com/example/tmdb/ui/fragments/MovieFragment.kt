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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tmdb.R
import com.example.tmdb.adapter.PageAdapter
import com.example.tmdb.databinding.FragmentMovieBinding
import com.example.tmdb.model.NetworkStatus
import com.example.tmdb.viewmodel.MoviesViewModel
import com.example.tmdb.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {
    private lateinit var viewModel : MoviesViewModel
    private lateinit var binding: FragmentMovieBinding
    private lateinit var adapter: PageAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var viewModelFactory: ViewModelFactory
    var page = 1
//    var movieType = "popular"
    var movieType: String? = null
    val TAG = "MovieFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = ViewModelFactory(activity!!.application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MoviesViewModel::class.java)
        movieType = "popular"
        viewModel.favoritesSelected.value = false
        if(savedInstanceState == null) {
            movieType?.let { viewModel.fetchMovies(page, it) }
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
            movieType?.let { viewModel.fetchMovies(page, it) }
            loadMovies()
        }
        val layoutManager = GridLayoutManager(context, 2)


        binding.recyclerView.layoutManager = layoutManager
        if(viewModel.favoritesSelected.value == false) {
            loadMovies()
        } else {
            favoritesLoader()
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun loadMovies() {
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
                viewModel.favoritesSelected.value = false
                viewModel.fetchMovies(page, movieType!!)
            }
            R.id.up_coming -> {
                movieType = "upcoming"
                viewModel.favoritesSelected.value = false
                viewModel.fetchMovies(page, movieType!!)
            }
            R.id.top_rated -> {
                movieType = "top_rated"
                viewModel.favoritesSelected.value = false
                viewModel.fetchMovies(page, movieType!!)
            }
            R.id.menu_favorites -> {
                viewModel.favoritesSelected.value = true
                favoritesLoader()
            }
            R.id.menu_refresh -> {
                swipeRefreshLayout.isRefreshing = true
                loadMovies()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun favoritesLoader() {
        viewModel.loadFavorites().observe(this, Observer { data ->
            adapter.submitList(data)
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
    }

}