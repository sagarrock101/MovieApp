package com.sagaRock101.tmdb.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.sagaRock101.tmdb.Constants
import com.sagaRock101.tmdb.MyApplication
import com.sagaRock101.tmdb.R
import com.sagaRock101.tmdb.adapter.ReviewAdapter
import com.sagaRock101.tmdb.adapter.TrailerAdapter
import com.sagaRock101.tmdb.databinding.FragmentMovieDetailBinding
import com.sagaRock101.tmdb.model.Movie
import com.sagaRock101.tmdb.model.MovieTrailer
import com.sagaRock101.tmdb.model.Review
import com.sagaRock101.tmdb.ui.interfaces.OnTrailerClickListener
import com.sagaRock101.tmdb.viewmodel.MoviesViewModel
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.getSearchBinding() != null)
            hideSoftKey()
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
                adapter.setItems(response.results as MutableList<MovieTrailer>)
                binding.trailerAdapter = adapter
            }
        })
    }

    private fun setReviewObserver() {
        var adapter = ReviewAdapter()
        viewModel.reviewsLD.observe(this, Observer {reviewListResponse ->
            if (reviewListResponse != null) {
                if(!reviewListResponse.results.isNullOrEmpty()) {
                    adapter.setItems(reviewListResponse.results as MutableList<Review>)
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
        var snack = Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT)
        var view = snack.view
        var tv = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        snack.show()
    }

    private fun share() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(Intent.EXTRA_TEXT, movie.title)
        intent.type = "text/string"
        startActivity(Intent.createChooser(intent, "Share the movie via"))
    }

    private fun hideSoftKey() {
        var imm =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(viewModel.getSearchBinding()?.etSearch?.rootView!!.windowToken,
            0)
    }
}