package com.example.tmdb.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmdb.GlideApp
import com.example.tmdb.MyApplication
import com.example.tmdb.R
import com.example.tmdb.adapter.TrailerAdapter
import com.example.tmdb.api.AppConstants
import com.example.tmdb.databinding.FragmentMovieDetailBinding
import com.example.tmdb.model.Movie
import com.example.tmdb.viewmodel.MoviesViewModel
import javax.inject.Inject


class MovieDetailFragment : Fragment() {
    private lateinit var binding: FragmentMovieDetailBinding

    val args: MovieDetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel : MoviesViewModel
    private lateinit var adapter: TrailerAdapter
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

        binding.fabShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(Intent.EXTRA_TEXT, movie.title)
            intent.type = "text/string"
            startActivity(Intent.createChooser(intent, "Share the movie via"))
            getInstalledApps(context!!)

        }


        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!!.application as MyApplication).appComponent.inject(this)
    }
    fun getInstalledApps(ctx: Context): Set<PackageInfo>? {
        val packageManager = ctx.packageManager
        val allInstalledPackages =
            packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        val filteredPackages: MutableSet<PackageInfo> = HashSet()
        val defaultActivityIcon = packageManager.defaultActivityIcon
        for (each in allInstalledPackages) {
            if (ctx.packageName == each.packageName) {
                continue  // skip own app
            }
            try { // add only apps with application icon
                val intentOfStartActivity =
                    packageManager.getLaunchIntentForPackage(each.packageName)
                        ?: continue
                val applicationIcon =
                    packageManager.getActivityIcon(intentOfStartActivity)
                if (applicationIcon != null && defaultActivityIcon != applicationIcon) {
                    filteredPackages.add(each)
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Log.i("MyTag", "Unknown package name " + each.packageName)
            }
        }
        return filteredPackages
    }
}