package com.example.tmdb.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.tmdb.MyApplication
import com.example.tmdb.R
import com.example.tmdb.adapter.FavoriteAdapter
import com.example.tmdb.databinding.FragmentFavoritesBinding
import com.example.tmdb.model.Movie
import com.example.tmdb.ui.interfaces.OnFavoriteClick
import com.example.tmdb.viewmodel.MoviesViewModel
import javax.inject.Inject

class MovieFavorite : Fragment(), OnFavoriteClick {

    lateinit var binding: FragmentFavoritesBinding
    lateinit var adapter: FavoriteAdapter

    @Inject
    lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
        setFavoriteObserver()
    }

    private fun setFavoriteObserver() {
        viewModel.getFavorites().observe(this, Observer { favoriteList ->
            if (!favoriteList.isNullOrEmpty()) {
                adapter.setItems(favoriteList)
            }
        })
    }

    private fun initAdapter() {
        adapter = FavoriteAdapter()
        adapter.setClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        binding.adapter = adapter
        setUpNavController()
        setToolBarTitle()
        return binding.root
    }

    private fun setToolBarTitle() {
        if(adapter.listItems.size > 1)
            binding.toolbar.title = getString(R.string.title_favorite)
        else binding.toolbar.title = getString(R.string.title_favorites)
    }

    private fun setUpNavController() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        val navController = activity!!.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(activity as AppCompatActivity, navController)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!!.application as MyApplication).appComponent.inject(this)
    }

    override fun onFavoriteClicked(item: Movie) {
        navigateToMovieDetail(item)
    }

    private fun navigateToMovieDetail(item: Movie) {
        val action = MovieFavoriteDirections.actionMovieFavoriteToMovieDetailFragment(
            item,
            item.title
        )
        findNavController().navigate(action)
    }

}