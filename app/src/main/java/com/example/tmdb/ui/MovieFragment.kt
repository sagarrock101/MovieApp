package com.example.tmdb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tmdb.R
import com.example.tmdb.adapter.PageAdapter
import com.example.tmdb.databinding.FragmentMovieBinding
import com.example.tmdb.model.NetworkStatus
import com.example.tmdb.viewmodel.PopularMoviesViewModel

class MovieFragment : Fragment() {
    private lateinit var viewModel : PopularMoviesViewModel
    private lateinit var binding: FragmentMovieBinding
    private lateinit var adapter: PageAdapter
    var page = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false)
        viewModel = ViewModelProviders.of(activity!!).get(PopularMoviesViewModel::class.java)
        val layoutManager = GridLayoutManager(context, 2)
        viewModel.fetchMovies(page)
        binding.recyclerView.layoutManager = layoutManager
        loadPopular()
        return binding.root
    }

    private fun loadPopular() {

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