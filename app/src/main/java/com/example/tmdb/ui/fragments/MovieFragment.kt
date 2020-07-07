package com.example.tmdb.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.work.WorkInfo
import com.example.tmdb.MyApplication
import com.example.tmdb.R
import com.example.tmdb.Utils
import com.example.tmdb.adapter.PageAdapter
import com.example.tmdb.databinding.FragmentMovieBinding
import com.example.tmdb.ui.activity.MainActivity
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
    private lateinit var layoutManager: GridLayoutManager


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val coroutineScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Main)
    }

    var page = 1
    var movieType: String? = null
    val TAG = this.javaClass.name


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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container,
            false)
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)

        setPagesLoadingObserver()
        setupSwipeToRefresh()
        loadMovies()
        setOnBackPressed()
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setOnBackPressed() {
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as AppCompatActivity).finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback);
    }

    private fun setupSwipeToRefresh() {
        swipeRefreshLayout = binding.swipeToRefresh
        swipeRefreshLayout.setOnRefreshListener {
            movieType?.let { viewModel.fetchMovies(page, it) }
            loadMovies()
        }
    }

    private fun setPagesLoadingObserver() {
        viewModel.getNetworkStatus()?.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }

    fun loadMovies() {
        binding.itemProgressBar.visibility = View.INVISIBLE
        swipeRefreshLayout.isRefreshing  = false
        initRv()
        setSpanLookUp()
        setTypeOfMovieObserver()
    }

    private fun setTypeOfMovieObserver() {
        movieType?.let {
            viewModel.movies.observe(this, Observer { data ->
                adapter.submitList(data)
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

    private fun setSpanLookUp() {
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val type: Int = adapter.getItemViewType(position)
                return if (type == R.layout.component_network_state_item) 2 else 1
            }
        }
    }

    private fun initRv() {
        adapter = PageAdapter()
        layoutManager = GridLayoutManager(context!!, 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
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