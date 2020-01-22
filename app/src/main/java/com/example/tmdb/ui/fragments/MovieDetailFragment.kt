package com.example.tmdb.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmdb.GlideApp
import com.example.tmdb.MyApplication
import com.example.tmdb.R
import com.example.tmdb.adapter.TrailerAdapter
import com.example.tmdb.api.AppConstants
import com.example.tmdb.databinding.FragmentMovieDetailBinding
import com.example.tmdb.model.Movie
import com.example.tmdb.viewmodel.MoviesViewModel
import java.util.*
import javax.inject.Inject


class MovieDetailFragment : Fragment() {
    private lateinit var binding: FragmentMovieDetailBinding
    private val MOVIE_TRAILER_THUMBNAIL_URL_PART_ONE = "https://img.youtube.com/watch?v="
    private val MOVIE_YOUTUBE_APP_TRAILER_URL = "vnd.youtube:"

    val args: MovieDetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel : MoviesViewModel
    private var adapter: TrailerAdapter? = null
    private var heartFlag = false
    lateinit var movie: Movie

    val TAG = "MovieDetailFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val pm = context!!.packageManager
        val apps = pm.getInstalledApplications(0)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container,
            false)

        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        val navController = activity!!.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(activity as AppCompatActivity,navController)
        adapter = TrailerAdapter()
        //            binding.movieItem = arguments!!.getParcelable("data")
//             movie = arguments!!.getParcelable("data")!!
        Log.e(TAG, "Navigation Host " + Navigation.findNavController(activity!!,R.id.myNavHostFragment).currentDestination)

        movie = args.movieData!!
        movie.id?.let { viewModel.getTrailers(it)
            binding.toolbarLayout.title = movie.title
            binding.tvMovieTitle.text = movie.title
            binding.cvSynopsis.text = movie.overview
            binding.detailStars.rating = movie.vote_average!!/2

        }
        GlideApp.with(binding.toolbarImage)
                        .load(AppConstants.IMAGE_URL + movie.backDropPath)
                        .placeholder(R.mipmap.ic_launcher_round)
                        .into(binding.toolbarImage)

        viewModel.trailersLiveData.observe(this, Observer { response ->
            adapter?.setData(response.results)
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

        binding.fabShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(Intent.EXTRA_TEXT, movie.title)
            intent.type = "text/string"
            startActivity(Intent.createChooser(intent, "Share the movie via"))
        }


        adapter?.onTrailerItemClick = {item ->
            val intent = Intent(Intent.ACTION_VIEW, item.key?.let { buildYoutubeAppVideoUrl(it) })
            startActivity(Intent.createChooser(intent, "Watch this trailer on"))

            if (intent.resolveActivity(Objects.requireNonNull(context)!!.packageManager)
                != null
            ) {
                startActivity(intent)
            }
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!!.application as MyApplication).appComponent.inject(this)
    }

    private fun buildYoutubeAppVideoUrl(key: String): Uri? {
        return Uri.parse(MOVIE_YOUTUBE_APP_TRAILER_URL + key)
    }


}