package com.example.tmdb.ui.fragments

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.tmdb.MyApplication
import com.example.tmdb.R
import com.example.tmdb.adapter.MoviesAdapter
import com.example.tmdb.databinding.FragmentMovieBinding
import com.example.tmdb.databinding.LayoutSearchBinding
import com.example.tmdb.viewmodel.MoviesViewModel
import com.example.tmdb.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class MovieFragment : Fragment(), View.OnClickListener {

    private var searchItem: ActionMenuItemView? = null

    @Inject
    lateinit var viewModel: MoviesViewModel
    private lateinit var binding: FragmentMovieBinding
    private lateinit var adapter: MoviesAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var layoutManager: GridLayoutManager? = null
    private var searchViewBinding: LayoutSearchBinding? = null
    private var container: FrameLayout? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val coroutineScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Main)
    }

    var page = 1
    var movieType = "popular"
    val TAG = this.javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            movieType.let { viewModel.fetchMovies(page, it) }
        }
        adapter = MoviesAdapter() {
            viewModel.retry()
        }
        setTypeOfMovieObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_movie, container,
            false
        )
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        setPagesLoadingObserver()
        setupSwipeToRefresh()
        loadMovies()
        setOnBackPressed()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as AppCompatActivity).finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewBinding = LayoutSearchBinding.inflate(layoutInflater)
        container = view.findViewById(R.id.fl_parent)
        searchViewBinding?.ibBack?.setOnClickListener(this)
    }

    private fun setupSwipeToRefresh() {
        swipeRefreshLayout = binding.swipeToRefresh
        swipeRefreshLayout.setOnRefreshListener {
            fetchMovies(page)
            loadMovies()
        }
    }

    private fun fetchMovies(currentPage: Int) {
        movieType?.let { viewModel.fetchMovies(currentPage, it) }
    }

    private fun setPagesLoadingObserver() {
        viewModel.getNetworkStatus()?.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }

    private fun loadMovies() {
        binding.itemProgressBar.visibility = View.INVISIBLE
        swipeRefreshLayout.isRefreshing = false
        initAdapter()
        setSpanLookUp()
    }

    private fun setTypeOfMovieObserver() {
        movieType?.let {
            viewModel.movies.observe(activity!!, Observer { data ->
                adapter.submitList(data)
            })
        }
    }

    private fun setSpanLookUp() {
        layoutManager?.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val type: Int = adapter.getItemViewType(position)
                return if (type == R.layout.component_network_state_item) 2 else 1
            }
        }
    }

    private fun initAdapter() {
        layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> {
                swipeRefreshLayout.isRefreshing = true
                loadMovies()
            }
            R.id.menu_sort -> {
                showFilterPopUpMenu()
            }
            R.id.menu_search -> {
                animateSearchBox()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun animateSearchBox() {
        var start = 16f
        var endRadius = binding.toolbar.width.coerceAtLeast(binding.toolbar.height)
        val point = IntArray(2)
        searchItem = binding.toolbar.findViewById(R.id.menu_search)
        searchItem!!.getLocationOnScreen(point)
        val (x, y) = point
        if (searchViewBinding != null) {
            container!!.addView(searchViewBinding!!.root)
            var params = searchViewBinding?.clSearchView?.layoutParams
            params?.width = binding.toolbar.width - 20
            params?.height = binding.toolbar.height
            var marginLayoutParams =
                searchViewBinding?.clSearchView?.layoutParams as ViewGroup.MarginLayoutParams
            marginLayoutParams.leftMargin = 10
            searchViewBinding?.clSearchView?.requestLayout()
            searchViewBinding?.clSearchView?.visibility = VISIBLE
            var animator = ViewAnimationUtils.createCircularReveal(
                searchViewBinding!!.root,
                x + 32,
                y - 16,
                start,
                endRadius.toFloat()
            )
            animator?.duration = 800
            animator?.start()
            animator?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    searchViewBinding?.etSearch?.requestFocus()
                    var imm =
                        activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(searchViewBinding?.etSearch, InputMethodManager.SHOW_IMPLICIT)
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
        }

    }


    private fun showFilterPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_sort) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.sort_menu, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.popular -> {
                        movieType = getString(R.string.movie_popular)
                        sortMovie(page, movieType)
                    }
                    R.id.up_coming -> {
                        movieType = getString(R.string.movie_up_coming)
                        sortMovie(page, movieType)
                    }
                    R.id.top_rated -> {
                        movieType = getString(R.string.movie_top_rated)
                        sortMovie(page, movieType)
                    }
                    R.id.menu_favorites -> {
                        movieType = getString(R.string.movie_favorites)
                        navigateToFavorite()
                    }
                    else -> {
                        false
                    }
                }
                true
            }
            show()
        }
    }

    private fun navigateToFavorite() {
        val action = MovieFragmentDirections.actionMovieFragmentToMovieFavorite()
        findNavController().navigate(action)
    }

    private fun sortMovie(page: Int, movieType: String) {
        viewModel.fetchMovies(page, movieType!!)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!!.application as MyApplication).appComponent.inject(this)
    }

    override fun onDestroyView() {
        layoutManager?.spanSizeLookup = null
        layoutManager = null
        viewModel.movies.removeObservers(this)
        super.onDestroyView()
    }

    override fun onClick(view: View?) {
        when (view) {
            searchViewBinding?.ibBack -> {
                hideSearchBox()
            }
        }
    }

    private fun hideSearchBox() {
        var start = binding.toolbar.width.coerceAtLeast(binding.toolbar.height)
        var endRadius = 16f
        val point = IntArray(2)
        searchItem = binding.toolbar.findViewById(R.id.menu_search)
        searchItem!!.getLocationOnScreen(point)
        val (x, y) = point
        var animator: Animator? = null
        if (searchViewBinding != null) {
            var params = searchViewBinding?.clSearchView?.layoutParams
            params?.width = binding.toolbar.width - 20
            params?.height = binding.toolbar.height
            var marginLayoutParams =
                searchViewBinding?.clSearchView?.layoutParams as ViewGroup.MarginLayoutParams
            marginLayoutParams.leftMargin = 10
            searchViewBinding?.clSearchView?.requestLayout()
            animator = ViewAnimationUtils.createCircularReveal(
                searchViewBinding!!.root,
                x + 32,
                y - 16,
                start.toFloat(),
                endRadius.toFloat()
            )
            animator?.duration = 800
            animator?.start()
        }

        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                searchViewBinding?.clSearchView?.visibility = GONE
                container!!.removeView(searchViewBinding!!.root)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })

    }

}