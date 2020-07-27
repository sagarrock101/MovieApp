package com.sagaRock101.tmdb.ui.fragments

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.PopupMenu
import android.widget.TextView.OnEditorActionListener
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.view.contains
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sagaRock101.tmdb.MyApplication
import com.sagaRock101.tmdb.R
import com.sagaRock101.tmdb.adapter.MoviesAdapter
import com.sagaRock101.tmdb.adapter.SearchSuggestionAdapter
import com.sagaRock101.tmdb.databinding.FragmentMovieBinding
import com.sagaRock101.tmdb.databinding.LayoutSearchBinding
import com.sagaRock101.tmdb.ui.interfaces.OnViewClickListener
import com.sagaRock101.tmdb.viewmodel.MoviesViewModel
import com.sagaRock101.tmdb.viewmodel.ViewModelFactory
import kotlinx.coroutines.*
import javax.inject.Inject


class MovieFragment : Fragment(), View.OnClickListener, OnViewClickListener {

    private var searchLayoutManager: GridLayoutManager? = null
    private var isSearchBackPressed: Boolean = false
    private var searchItem: ActionMenuItemView? = null

    @Inject
    lateinit var viewModel: MoviesViewModel
    private lateinit var binding: FragmentMovieBinding
    private lateinit var adapter: MoviesAdapter
    private lateinit var searchAdapter: MoviesAdapter
    private lateinit var searchSuggestionAdapter: SearchSuggestionAdapter
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

    var list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            if (viewModel.rvSearchVisibility != VISIBLE)
                movieType.let { viewModel.fetchMovies(page, it) }
        }
        adapter = MoviesAdapter() {
            viewModel.retry()
        }
        searchAdapter = MoviesAdapter() {
            viewModel.retrySearch()
        }
        searchSuggestionAdapter = SearchSuggestionAdapter()
        searchSuggestionAdapter.setItems(list)
        searchSuggestionAdapter.setCloseListener(this)
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

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        container = view.findViewById(R.id.fl_parent)
        if (viewModel.getSearchBinding() == null)
            searchViewBinding = LayoutSearchBinding.inflate(layoutInflater)
        else {
            searchViewBinding = viewModel.searchViewBinding
            if (searchViewBinding?.clSearchView?.visibility == VISIBLE) {
                searchViewBinding?.clSearchView?.visibility = VISIBLE
                container?.addView(searchViewBinding!!.root)
                searchViewBinding?.etSearch?.requestFocus()
                isSearchBackPressed = false
                if (viewModel.rvSearchVisibility == VISIBLE) {
                    binding.recyclerView.visibility = GONE
                }

                if (viewModel.rvSearchVisibility != null)
                    binding.rvSearch.visibility = viewModel.rvSearchVisibility!!

                if (viewModel.searchListState != null) {
                    binding.rvSearch.layoutManager?.onRestoreInstanceState(viewModel.searchListState)
                    viewModel.listState = null
                }
//                showSoftKey()
            }
        }
        searchViewBinding?.ibBack?.setOnClickListener(this)
        searchViewBinding?.etSearch?.setOnEditorActionListener(
            OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || event != null &&
                    event.action == KeyEvent.ACTION_DOWN
                    && event.keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    if (event == null || !event.isShiftPressed) {
                        // the user is done typing.
                        viewModel.searchMovie(searchViewBinding?.etSearch?.text.toString())
                        binding.rvSearch.visibility = VISIBLE
                        binding.recyclerView.visibility = GONE
                        hideSoftKey()
                        setSearchNetworkObserver()
                        return@OnEditorActionListener true // consume.
                    }
                }
                false // pass on to other listeners.
            }
        )

        searchViewBinding?.etSearch?.doAfterTextChanged { text ->
            var query = text.toString()
            showSuggestionRv()
            searchQuery(query)
        }

       viewModel.searchSuggestionLD.observe(this, Observer { data ->
           if(!data.results.isNullOrEmpty()) {
               data.results.forEachIndexed { index, movie ->
                   list.add(movie.title!!)
               }
               list.add("")
               searchSuggestionAdapter.setItems(list)
               showSuggestionRv()
           }
       })
    }

    private fun searchQuery(query: String) {
        coroutineScope.launch {
            delay(500)
            if(query.length >= 3)
                viewModel.searchSuggestion(query)
        }
    }

    private fun showSuggestionRv() {
        addLayoutAnimationToRv(binding.rvSearchSuggestion, R.anim.layout_animation_fall_down)
        coroutineScope.launch {
            delay(300)
            binding.rvSearchSuggestion.translationY = 0f
        }
        binding.rvSearchSuggestion.visibility = VISIBLE
        binding.rvSearchSuggestion.scheduleLayoutAnimation()
    }

    private fun addLayoutAnimationToRv(
        rv: RecyclerView,
        layoutAnimation: Int
    ) {
        var controller = AnimationUtils.loadLayoutAnimation(rv.context, layoutAnimation)
        rv.layoutAnimation = controller
    }

    private fun setSearchNetworkObserver() {
        viewModel.getSearchNetworkStatus()?.observe(viewLifecycleOwner, Observer {
            searchAdapter.setNetworkState(it)
        })
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
        swipeRefreshLayout.isRefreshing = false
        initRv()
    }

    private fun setTypeOfMovieObserver() {
        movieType?.let {
            viewModel.movies.observe(activity!!, Observer { data ->
                adapter.submitList(data)
            })
        }

        viewModel.searchMovies.observe(activity!!, Observer { data ->
            searchAdapter.submitList(data)
        })
    }

    private fun setSpanLookUp(
        layoutManager: GridLayoutManager?,
        adapter: MoviesAdapter
    ) {
        layoutManager?.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val type: Int = adapter.getItemViewType(position)
                return if (type == R.layout.component_network_state_item) 2 else 1
            }
        }


    }

    private fun initRv() {
        layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        setSpanLookUp(layoutManager, adapter)
        searchLayoutManager = GridLayoutManager(activity, 2)
        binding.rvSearch.layoutManager = searchLayoutManager
        binding.rvSearch.adapter = searchAdapter
        setSpanLookUp(searchLayoutManager, searchAdapter)

        binding.rvSearchSuggestion.adapter = searchSuggestionAdapter
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
            if (!container!!.contains(searchViewBinding!!.root))
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
                    isSearchBackPressed = false
                    showSoftKey()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
        }

    }


    private fun showSoftKey() {

        var imm =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchViewBinding?.etSearch, InputMethodManager.SHOW_IMPLICIT)
    }


    private fun showFilterPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_sort) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.sort_menu, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.popular -> {
                        movieType = getString(R.string.movie_popular)
                        binding.recyclerView.visibility = VISIBLE
                        binding.rvSearch.visibility = GONE
                        sortMovie(page, movieType)
                    }
                    R.id.up_coming -> {
                        movieType = getString(R.string.movie_up_coming)
                        binding.recyclerView.visibility = VISIBLE
                        binding.rvSearch.visibility = GONE
                        sortMovie(page, movieType)
                    }
                    R.id.top_rated -> {
                        movieType = getString(R.string.movie_top_rated)
                        binding.recyclerView.visibility = VISIBLE
                        binding.rvSearch.visibility = GONE
                        sortMovie(page, movieType)
                    }
                    R.id.menu_favorites -> {
                        movieType = getString(R.string.movie_favorites)
                        binding.recyclerView.visibility = VISIBLE
                        binding.rvSearch.visibility = GONE
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
        viewModel.searchMovies.removeObservers(this)
        if (searchViewBinding != null)
            viewModel.setSearchBinding(searchViewBinding!!)
        viewModel.searchListState = binding.rvSearch.layoutManager?.onSaveInstanceState()
        viewModel.rvSearchVisibility = binding.rvSearch.visibility
        container!!.removeView(searchViewBinding!!.root)
        super.onDestroyView()
    }

    private fun hideSoftKey() {
        var imm =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            searchViewBinding?.etSearch?.rootView!!.windowToken,
            0
        )
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
        var animatorSet = AnimatorSet()
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
            if (binding.rvSearchSuggestion.visibility == VISIBLE)
                hideSuggestionRv()
            animator.duration = 800
            animator.start()


        }

        binding.rvSearchSuggestion.layoutAnimationListener = object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        }

        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                searchViewBinding?.clSearchView?.visibility = GONE
                container!!.removeView(searchViewBinding!!.root)
                binding.rvSearch.visibility = GONE
                binding.recyclerView.visibility = VISIBLE
                isSearchBackPressed = true
                viewModel.rvSearchVisibility = binding.rvSearch.visibility
//                binding.rvSearchSuggestion.visibility = GONE

            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
    }

    private fun hideSuggestionRv() {
        var slideUpAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        binding.rvSearchSuggestion.startAnimation(slideUpAnimation)
        addLayoutAnimationToRv(binding.rvSearchSuggestion, R.anim.layout_animation_go_up)
        slideUpAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.rvSearchSuggestion.visibility = GONE
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
    }



    override fun onClickView(view: Int?, data: Any?) {
        when(view) {
            R.id.cl_search_item -> {
                if(data is String) {
                    setSearchTextToEditText(data)
                }
            }
            R.id.iv_close -> {
                hideSuggestionRv()
            }
        }
    }

    private fun setSearchTextToEditText(query: String) {
        searchQuery(query)
        hideSuggestionRv()
        searchViewBinding?.etSearch?.setText(query)
    }

}