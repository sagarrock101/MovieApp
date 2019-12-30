package com.example.tmdb.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.tmdb.R

import com.example.tmdb.adapter.PageAdapter
import com.example.tmdb.viewmodel.MoviesViewModel
import com.example.tmdb.databinding.ActivityMainBinding
import com.example.tmdb.ui.fragments.SplashFragment


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    private lateinit var adapter: PageAdapter
    private lateinit var viewModel : MoviesViewModel

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        viewModel = ViewModelProviders.of(this).get(MoviesViewModel::class.java)
//        navController = this.findNavController(R.id.myNavHostFragment)
//        NavigationUI.setupActionBarWithNavController(this,navController)
//        supportActionBar!!.hide()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_main, SplashFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        navController.popBackStack()
    }



}
