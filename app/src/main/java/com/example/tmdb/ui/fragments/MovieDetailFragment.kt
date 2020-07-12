package com.example.tmdb.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.example.tmdb.Constants
import com.example.tmdb.MyApplication
import com.example.tmdb.R
import com.example.tmdb.adapter.ReviewAdapter
import com.example.tmdb.adapter.TrailerAdapter
import com.example.tmdb.databinding.FragmentMovieDetailBinding
import com.example.tmdb.model.Movie
import com.example.tmdb.model.MovieTrailer
import com.example.tmdb.ui.interfaces.OnTrailerClickListener
import com.example.tmdb.viewmodel.MoviesViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class MovieDetailFragment : Fragment(), AppBarLayout.OnOffsetChangedListener,
    OnTrailerClickListener, View.OnClickListener {

    private lateinit var binding: FragmentMovieDetailBinding

    private val args: MovieDetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel : MoviesViewModel
    private var heartFlag = false
    lateinit var movie: Movie

    private val TAG = this.javaClass.name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container,
            false)
        setUpNavController()
        setViewClickListeners()
        callApi()
        setReviewObserver()
        setTrailerObserver()
        checkForFavorite()
        return binding.root
    }

    private fun setUpNavController() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        val navController = activity!!.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(activity as AppCompatActivity, navController)
    }

    private fun callApi() {
        movie = args.movieData!!
        movie.id?.let {
            viewModel.getTrailers(it)
            viewModel.fetchReviews(it)
            viewModel.getMovieCheck(it)
            binding.movieItem = movie
            binding.detailStars.rating = movie.vote_average!!/2
        }
    }

    private fun setViewClickListeners() {
        binding.fab.setOnClickListener(this)
        binding.appBar.addOnOffsetChangedListener(this)
        binding.fabShare.setOnClickListener(this)
    }

    private fun checkForFavorite() {
        viewModel.getMovieFromDb().observe(this, Observer { liveData ->
            if(liveData != null) {
                heartFlag = if(movie.id == liveData.id) {
                    binding.fab.setImageResource(R.drawable.ic_like)
                    true
                } else {
                    binding.fab.setImageResource(R.drawable.ic_heart)
                    false
                }
            }
        })
    }

    private fun setTrailerObserver() {
        var adapter = TrailerAdapter()
        adapter.setTrailerListener(this)
        viewModel.trailersLiveData.observe(this, Observer { response ->
            if(!response.results.isNullOrEmpty()) {
                adapter.setItems(response.results)
                binding.trailerAdapter = adapter
            }
        })
    }

    private fun setReviewObserver() {
        var adapter = ReviewAdapter()
        viewModel.reviewsLD.observe(this, Observer {reviewListResponse ->
            if (reviewListResponse != null) {
                if(!reviewListResponse.results.isNullOrEmpty()) {
                    adapter.setItems(reviewListResponse.results)
                    binding.reviewAdapter = adapter
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!!.application as MyApplication).appComponent.inject(this)
    }

    private fun buildYoutubeAppVideoUrl(key: String): Uri? {
        return Uri.parse(Constants.MOVIE_YOUTUBE_APP_TRAILER_URL + key)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if(verticalOffset == 0) {
            binding.fab.animate(View.VISIBLE, 1.0f)
            binding.fabShare.animate(View.VISIBLE, 1.0f)
        } else {
            binding.fab.animate(View.GONE, 0.01f)
            binding.fabShare.animate(View.GONE, 0.01f)
        }

    }

    private fun View.animate(visibility: Int, alpha: Float) {
        this.apply {
            this.visibility = visibility
            animate().alpha(alpha).duration = 400
        }
    }

    private fun startYouTube(item: MovieTrailer) {
        val intent = Intent(Intent.ACTION_VIEW, item.key?.let { buildYoutubeAppVideoUrl(it) })
        startActivity(intent)
    }

    override fun onTrailerClicked(item: MovieTrailer) {
        startYouTube(item)
    }

    override fun onClick(view: View?) {
        when(view) {
            binding.fab -> {
               setHeart()
            }
            binding.fabShare -> {
                share()
            }
        }
    }

    private fun setHeart() {
        heartFlag = if(heartFlag) {
            binding.fab.setImageResource(R.drawable.ic_heart)
            viewModel.deleteFromDb(movie.id!!)
            showSnack(R.string.removed_from_favorites)
            false
        } else {
            binding.fab.setImageResource(R.drawable.ic_like)
            viewModel.insertToDb(movie)
            showSnack(R.string.added_to_favorites)
            true
        }
    }

    private fun showSnack(msg: Int) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun share() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(Intent.EXTRA_TEXT, movie.title)
        intent.type = "text/string"
        startActivity(Intent.createChooser(intent, "Share the movie via"))
    }
}