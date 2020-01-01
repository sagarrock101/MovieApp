package com.example.tmdb.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmdb.R
import com.example.tmdb.adapter.TrailerAdapter
import com.example.tmdb.databinding.FragmentMovieDetailBinding
import com.example.tmdb.model.Movie
import com.example.tmdb.viewmodel.MoviesViewModel

class MovieDetailFragment : Fragment() {
    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var viewModel : MoviesViewModel
    private lateinit var adapter: TrailerAdapter
    private var heartFlag = false
    lateinit var movie: Movie

    val TAG = "MovieDetailFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(MoviesViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container,
            false)

        if(arguments != null) {
            binding.movieItem = arguments!!.getParcelable("data")
             movie = arguments!!.getParcelable("data")!!
            movie.id?.let { viewModel.getTrailers(it) }

        }

        viewModel.trailersLiveData.observe(this, Observer { response ->
            adapter = TrailerAdapter(response.results)
            binding.rvTrailerThumbnail.adapter = adapter
            binding.rvTrailerThumbnail.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false)
        })
        movie.id?.let { viewModel.getMovieCheck(it) }
        viewModel.getMovieFromDb().observe(this, Observer { liveData ->
           if(liveData != null) {
               if(movie.id == liveData.id) {
                   binding.fab.setImageResource(R.drawable.ic_like)
                   heartFlag = true
                   Toast.makeText(context, "Inserted", Toast.LENGTH_SHORT).show()
               } else {
                   Toast.makeText(context, "not inserted", Toast.LENGTH_SHORT).show()
                   binding.fab.setImageResource(R.drawable.ic_heart)
                   heartFlag = false
               }
           }
        })

        binding.fab.setOnClickListener {
            if(heartFlag) {
                binding.fab.setImageResource(R.drawable.ic_heart)
                viewModel.deleteFromDb(movie.id!!)
                heartFlag = false
            } else {
                binding.fab.setImageResource(R.drawable.ic_like)
                viewModel.insertToDb(movie)
                heartFlag = true
            }
        }


        return binding.root
    }


}