package com.example.tmdb

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager

import com.example.tmdb.adapter.PageAdapter
import com.example.tmdb.viewmodel.PopularMoviesViewModel
import com.example.tmdb.databinding.ActivityMainBinding
import com.example.tmdb.model.NetworkStatus
import com.example.tmdb.model.PopularMovieResults


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    private lateinit var adapter: PageAdapter
    private lateinit var viewModel : PopularMoviesViewModel

    private lateinit var binding: ActivityMainBinding
    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(PopularMoviesViewModel::class.java)

        viewModel.fetchMovies(page)
        val layoutManager = GridLayoutManager(baseContext, 2)
        binding.recyclerView.layoutManager = layoutManager

        loadPopular()

        binding.recyclerView.addOnScrollListener(object : EndlessRecyclerOnScrollListener(){
            override fun onLoadMore() {

            }

        })




    }
    fun loadPopular() {

        adapter = PageAdapter()

        viewModel.popularMovies.observe(this, Observer { data ->
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


}
