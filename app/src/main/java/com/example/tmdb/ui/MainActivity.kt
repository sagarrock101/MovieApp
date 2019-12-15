package com.example.tmdb.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tmdb.EndlessRecyclerOnScrollListener
import com.example.tmdb.R

import com.example.tmdb.adapter.PageAdapter
import com.example.tmdb.viewmodel.PopularMoviesViewModel
import com.example.tmdb.databinding.ActivityMainBinding
import com.example.tmdb.model.NetworkStatus


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    private lateinit var adapter: PageAdapter
    private lateinit var viewModel : PopularMoviesViewModel

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(PopularMoviesViewModel::class.java)
        navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }



}
