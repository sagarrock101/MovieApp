package com.sagaRock101.tmdb.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.sagaRock101.tmdb.MyApplication
import com.sagaRock101.tmdb.R
import com.sagaRock101.tmdb.adapter.FavoriteAdapter
import com.sagaRock101.tmdb.databinding.FragmentFavoritesBinding
import com.sagaRock101.tmdb.model.Movie
import com.sagaRock101.tmdb.ui.interfaces.OnFavoriteClick
import com.sagaRock101.tmdb.viewmodel.MoviesViewModel
import javax.inject.Inject

class MovieFavorite : Fragment(), OnFavoriteClick {

    lateinit var binding: FragmentFavoritesBinding
    private var adapter: FavoriteAdapter? = null

    @Inject
    lateinit var viewModel: MoviesViewModel

    val TAG = this.javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
    }

    private fun setFavoriteObserver() {
        viewModel.getFavorites().observe(this, Observer { favoriteList ->
            if (!favoriteList.isNullOrEmpty()) {
                adapter?.setItems(favoriteList as MutableList<Movie>)
                viewVisiblitiy(true)
                if (adapter?.listItems?.size!! == 1)
                    binding.toolbar.title = getString(R.string.title_favorite)
                else
                    binding.toolbar.title = getString(R.string.title_favorites)

            } else {
                adapter?.listItems?.clear()
                viewVisiblitiy(false)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel.listState != null) {
            binding.rvFavorites.layoutManager?.onRestoreInstanceState(viewModel.listState)
            viewModel.listState = null
        }
    }

    private fun initAdapter() {
        adapter = FavoriteAdapter()
        adapter?.setClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        binding.adapter = adapter
        setUpNavController()
        setFavoriteObserver()
        return binding.root
    }

    private fun viewVisiblitiy(gone: Boolean) {
        if (gone) {
            binding.tvNoFavorites.visibility = GONE
            binding.rvFavorites.visibility = VISIBLE
        } else {
            binding.tvNoFavorites.visibility = VISIBLE
            binding.rvFavorites.visibility = GONE
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.getFavorites().removeObservers(this)
        viewModel.listState = binding.rvFavorites.layoutManager?.onSaveInstanceState()
    }

}