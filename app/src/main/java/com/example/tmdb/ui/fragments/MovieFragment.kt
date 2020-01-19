package com.example.tmdb.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tmdb.MyApplication
import com.example.tmdb.R
import com.example.tmdb.adapter.PageAdapter
import com.example.tmdb.databinding.FragmentMovieBinding
import com.example.tmdb.model.NetworkStatus
import com.example.tmdb.viewmodel.MoviesViewModel
import com.example.tmdb.viewmodel.ViewModelFactory
import javax.inject.Inject


class MovieFragment : Fragment() {

    @Inject
    lateinit var viewModel : MoviesViewModel
    private lateinit var binding: FragmentMovieBinding
    private lateinit var adapter: PageAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    var page = 1
    var movieType: String? = null
    val TAG = "MovieFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieType = "popular"
        if(savedInstanceState == null) {
            movieType?.let { viewModel.fetchMovies(page, it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        swipeRefreshLayout = binding.swipeToRefresh
        swipeRefreshLayout.setOnRefreshListener {
            movieType?.let { viewModel.fetchMovies(page, it) }
            loadMovies()
        }
        val layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.layoutManager = layoutManager
        loadMovies()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun loadMovies() {
        binding.itemProgressBar.visibility = View.INVISIBLE
        swipeRefreshLayout.isRefreshing  = false
        adapter = PageAdapter()
        Log.e(TAG, "loadMovies")
        movieType?.let {
            viewModel.popularMoviesLiveData.observe(this, Observer { data ->
                adapter.submitList(data)
            })
        }
        binding.recyclerView.adapter = adapter

        viewModel.getMoviesStatus().observe(this, Observer { networkStatus ->
            when(networkStatus) {
                NetworkStatus.SUCCESS -> {
                    binding.itemProgressBar.visibility = View.INVISIBLE
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
                viewModel.fetchMovies(page, movieType!!)
            }
            R.id.up_coming -> {
                movieType = "upcoming"
                viewModel.fetchMovies(page, movieType!!)
            }
            R.id.top_rated -> {
                movieType = "top_rated"
                viewModel.fetchMovies(page, movieType!!)
            }
            R.id.menu_favorites -> {
                movieType = "favorites"
                viewModel.fetchMovies(page, movieType!!)
            }
            R.id.menu_refresh -> {
                swipeRefreshLayout.isRefreshing = true
                loadMovies()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!!.application as MyApplication).appComponent.inject(this)
    }

}