package com.example.tmdb.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tmdb.MyApplication
import com.example.tmdb.R
import com.example.tmdb.adapter.PageAdapter
import com.example.tmdb.databinding.FragmentMovieBinding
import com.example.tmdb.ui.MainActivity
import com.example.tmdb.viewmodel.MoviesViewModel
import com.example.tmdb.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class MovieFragment : Fragment() {

    @Inject
    lateinit var viewModel : MoviesViewModel
    private lateinit var binding: FragmentMovieBinding
    private lateinit var adapter: PageAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @Inject
    lateinit var viewModelFactory: ViewModelFactory



    val coroutineScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Main)
    }

    var page = 1
    var movieType: String? = null
    val TAG = "MovieFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieType = "popular"
        if(savedInstanceState == null) {
            movieType?.let { viewModel.fetchMovies(page, it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        viewModel.getNetworkStatus()?.observe(this, Observer {
            adapter.setNetworkState(it)
        })
        swipeRefreshLayout = binding.swipeToRefresh
        swipeRefreshLayout.setOnRefreshListener {
            movieType?.let { viewModel.fetchMovies(page, it) }
            loadMovies()
        }
        loadMovies()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun loadMovies() {
        var layoutManager: GridLayoutManager? = null
        binding.itemProgressBar.visibility = View.INVISIBLE
        swipeRefreshLayout.isRefreshing  = false
        adapter = PageAdapter()
        layoutManager = GridLayoutManager(context!!, 2)

        binding.recyclerView.layoutManager = layoutManager

        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val type: Int = adapter.getItemViewType(position)
                return if (type == R.layout.component_network_state_item) 2 else 1
            }
        }
        binding.recyclerView.adapter = adapter
        Log.e(TAG, "loadMovies")
        movieType?.let {
            viewModel.movies.observe(this, Observer { data ->
                adapter.submitList(data)
//                val position = layoutManager.findFirstCompletelyVisibleItemPosition()
//                if(position != RecyclerView.NO_POSITION) {
//
//                }
             if(!MainActivity.firstTime) {
                 coroutineScope.launch {
                     delay(500)
                     binding.recyclerView.scrollToPosition(0)
                 }
                 MainActivity.firstTime = true
             }
            })
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.popular -> {
                movieType = "popular"
                viewModel.fetchMovies(page, movieType!!)
            }
            R.id.up_coming -> {
                movieType = "upcoming"
                viewModel.fetchMovies(page, movieType!!)
            }
            R.id.top_rated -> {
                movieType = "top_rated"
                viewModel.fetchMovies(page, movieType!!)
            }
            R.id.menu_favorites -> {
                movieType = "favorites"
                viewModel.fetchMovies(page, movieType!!)
            }
            R.id.menu_refresh -> {
                swipeRefreshLayout.isRefreshing = true
                loadMovies()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!!.application as MyApplication).appComponent.inject(this)
    }

}